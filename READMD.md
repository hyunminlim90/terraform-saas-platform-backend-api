# Sprout Backend README

> Terraform/OpenTofu SaaS 플랫폼의 백엔드 서버입니다. 이 문서는 업로드된 `sprout` 소스 코드를 기준으로 작성했습니다.

## 1. 프로젝트 개요

`sprout`는 AWS 리소스를 웹 UI에서 설계·저장·조회하고, OpenTofu 실행 파일을 생성한 뒤 `plan`, `apply`, `destroy`를 비동기 실행하는 Spring Boot 백엔드입니다.

주요 역할은 다음과 같습니다.

- AWS 임시 자격 증명 등록 및 계정 조회
- AWS 리소스 조회를 위한 Python/boto3 스크립트 생성·실행
- VPC, Subnet, Instance, Security Group 등 AWS 리소스 설정 저장
- OpenTofu `resource` / `module` 파일 생성
- OpenTofu `init`, `plan`, `apply`, `destroy` 실행
- 실행 로그를 SSE 방식으로 프론트엔드에 스트리밍
- Redis/Redisson 세마포어를 통한 동시 실행 제한
- MySQL/JPA 기반 리소스 및 계정 데이터 저장

## 2. 기술 스택

| 구분 | 내용 |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.3 |
| Build | Maven Wrapper |
| Web | Spring MVC, Servlet Filter, SSE `SseEmitter` |
| Security | Spring Security, CSRF, JWT 검증 필터 |
| Persistence | Spring Data JPA, MySQL |
| Async | `ThreadPoolTaskExecutor`, `CompletableFuture`, `@Async` |
| Distributed Control | Redis, Redisson `RSemaphore` |
| IaC Runtime | OpenTofu CLI |
| AWS Integration | Python 3, boto3 스크립트 생성/실행 |
| Packaging | Spring Boot executable jar |

## 3. 전체 아키텍처

```text
Frontend
  ↓ HTTPS + JWT Cookie + CSRF
Spring Boot Backend
  ├─ Controller Layer
  │   ├─ STS / Account API
  │   ├─ Resource Save/Load API
  │   ├─ Draft Version API
  │   └─ OpenTofu Exec API
  ├─ Service Layer
  │   ├─ AWS credential/account 처리
  │   ├─ Python boto3 script 생성/실행
  │   ├─ OpenTofu resource/module 생성
  │   ├─ OpenTofu plan/apply/destroy 실행
  │   └─ JSON 저장/조회/다운로드
  ├─ Handler Layer
  │   ├─ Entity Builder
  │   ├─ Python Script Builder
  │   ├─ Tofu Resource Builder
  │   └─ Tofu Module Builder
  ├─ Persistence
  │   └─ MySQL + JPA Entity/Repository
  ├─ Concurrency Control
  │   └─ Redis/Redisson Semaphore
  └─ File Workspace
      ├─ /home/ec2-user/opentofu/workspace
      └─ /home/ec2-user/opentofu/download
```

## 4. 주요 기능

### 4.1 AWS 계정/STS 처리

관련 패키지:

```text
controller/sts
service/implementations/AccountService.java
service/implementations/PythonBoto.java
service/implementations/DeleteStsUtils.java
handler/python/implementations/AwsSts.java
handler/python/implementations/AwsStsVerify.java
handler/entity/entity/sts/entity/StsEntity.java
```

역할:

- 프론트엔드에서 전달받은 AWS 임시 자격 증명을 검증
- boto3 기반 STS 호출 스크립트를 동적으로 생성
- AWS Account ID, alias, roleName 등 계정 정보를 저장/조회
- 계정 삭제 및 workspace 정리 API 제공

### 4.2 AWS 리소스 조회

관련 패키지:

```text
controller/resource/load
service/implementations/PythonLoadResource.java
handler/python/implementations/Aws*Boto.java
```

지원 리소스:

- `aws_vpc`
- `aws_vpc_ipam_pool`
- `aws_instance`
- `aws_subnet`
- `aws_security_group`
- `aws_internet_gateway`
- `aws_route_table`
- `aws_route_table_association`

동작 방식:

```text
API 요청
  → ResourceDto 파싱
  → 리소스별 PythonHandler 선택
  → boto3 Python 파일 생성
  → python3 실행
  → AWS 리소스 정보 조회
  → 응답 또는 후속 저장 흐름으로 전달
```

### 4.3 리소스 저장/조회 및 Draft Version

관련 패키지:

```text
controller/resource/save
controller/resource/load
controller/draft/load
controller/draft/delete
controller/draft/duplicate
service/implementations/EntitySaveResource.java
service/implementations/LoadDraftVersion.java
service/implementations/DeleteDraftVersion.java
service/implementations/DuplicateDraftVersions.java
handler/entity
```

역할:

- 프론트엔드에서 구성한 AWS 리소스 정의 저장
- 저장된 리소스 조회
- draft version 로드/삭제/복제
- 리소스별 Entity/DTO 변환

### 4.4 OpenTofu 파일 생성

관련 패키지:

```text
service/implementations/TofuResource.java
service/implementations/TofuModule.java
handler/tofu/resource/implementations
handler/tofu/module/implementations
```

파일 생성 위치:

```text
/home/ec2-user/opentofu/workspace/{authEmailId}/{accountUuid}/{regionCode}/tofu_resource/{moduleName}/main.tf
/home/ec2-user/opentofu/workspace/{authEmailId}/{accountUuid}/{regionCode}/tofu_module/{moduleName}/main.tf
```

역할:

- 리소스별 `resource` 블록 생성
- 리소스별 `module` 블록 생성
- 일부 리소스는 Terraform/OpenTofu state 형태 JSON 생성 로직도 포함

### 4.5 OpenTofu 실행

관련 패키지:

```text
controller/emitter
service/implementations/ExecTofuPlan.java
service/implementations/ExecTofuApply.java
service/implementations/ExecTofuPlanDestroy.java
service/implementations/ExecTofuDestroy.java
util/ProcessUtils.java
```

실행 명령:

| 작업 | 내부 명령 |
|---|---|
| Plan | `tofu init` → `tofu plan` |
| Apply | `tofu init` → `tofu apply -auto-approve` |
| Plan Destroy | `tofu init` → `tofu plan -destroy` |
| Destroy | `tofu init` → `tofu destroy -auto-approve` |

실행 결과는 `SseEmitter`로 프론트엔드에 실시간 전송됩니다.

## 5. 동시성 제어

비동기 작업은 `AsyncConfig`에서 정의한 `ThreadPoolTaskExecutor`를 사용합니다.

| 설정 | 값 |
|---|---:|
| corePoolSize | 40 |
| maxPoolSize | 200 |
| queueCapacity | 2000 |
| keepAliveSeconds | 60 |
| rejectedExecutionHandler | CallerRunsPolicy |

OpenTofu 실행은 Redis/Redisson 세마포어로 제한됩니다.

| Semaphore Key | Permit | 용도 |
|---|---:|---|
| `tofu:plan` | 6 | `tofu plan` 동시 실행 제한 |
| `tofu:apply` | 4 | `tofu apply` 동시 실행 제한 |
| `tofu:destroy` | 3 | `tofu destroy`, `tofu plan -destroy` 동시 실행 제한 |

세마포어 획득 대기 시간은 60초입니다. 획득 실패 시 SSE로 queue full 메시지를 전달하고 작업을 실패 처리합니다.

## 6. API 요약

### 6.1 공통 인증/계정 API

| Method | Path | Controller |
|---|---|---|
| POST | `/api/v1/auth/csrf-token` | `CsrfRequest` |
| POST | `/api/v1/register/temporary-credential` | `ClientRequest` |
| POST | `/api/v1/request/aws-account/delete` | `DeleteRequest` |
| POST | `/api/v1/request/aws-accounts` | `GetAccountRequest` |
| POST | `/api/v1/request/aws-accounts/auto-clear/parallel` | `AutoClearRequest` |
| POST | `/api/v1/request/aws-resources/downloads` | `DownloadTerraformRequest` |
| POST | `/api/v1/request/workspace/empty/discard` | `DeleteRequest` |

### 6.2 AWS 리소스 API

리소스별로 동일한 패턴의 API가 존재합니다.

| Resource | API |
|---|---|
| `aws_vpc` | `POST /api/v1/request/aws-resources/delete-draft/aws_vpc`<br>`POST /api/v1/request/aws-resources/duplicate-draft/aws_vpc`<br>`POST /api/v1/request/aws-resources/exec-tofu-apply/aws_vpc`<br>`POST /api/v1/request/aws-resources/exec-tofu-destroy/aws_vpc`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan-destroy/aws_vpc`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan/aws_vpc`<br>`POST /api/v1/request/aws-resources/issue-unique-id/aws_vpc`<br>`POST /api/v1/request/aws-resources/load-draft-version/aws_vpc`<br>`POST /api/v1/request/aws-resources/load-resource/aws_vpc`<br>`POST /api/v1/request/aws-resources/save-resource/aws_vpc` |
| `aws_vpc_ipam_pool` | `POST /api/v1/request/aws-resources/delete-draft/aws_vpc_ipam_pool`<br>`POST /api/v1/request/aws-resources/duplicate-draft/aws_vpc_ipam_pool`<br>`POST /api/v1/request/aws-resources/exec-tofu-apply/aws_vpc_ipam_pool`<br>`POST /api/v1/request/aws-resources/exec-tofu-destroy/aws_vpc_ipam_pool`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan-destroy/aws_vpc_ipam_pool`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan/aws_vpc_ipam_pool`<br>`POST /api/v1/request/aws-resources/issue-unique-id/aws_vpc_ipam_pool`<br>`POST /api/v1/request/aws-resources/load-draft-version/aws_vpc_ipam_pool`<br>`POST /api/v1/request/aws-resources/load-resource/aws_vpc_ipam_pool`<br>`POST /api/v1/request/aws-resources/save-resource/aws_vpc_ipam_pool` |
| `aws_instance` | `POST /api/v1/request/aws-resources/delete-draft/aws_instance`<br>`POST /api/v1/request/aws-resources/duplicate-draft/aws_instance`<br>`POST /api/v1/request/aws-resources/exec-tofu-apply/aws_instance`<br>`POST /api/v1/request/aws-resources/exec-tofu-destroy/aws_instance`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan-destroy/aws_instance`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan/aws_instance`<br>`POST /api/v1/request/aws-resources/issue-unique-id/aws_instance`<br>`POST /api/v1/request/aws-resources/load-draft-version/aws_instance`<br>`POST /api/v1/request/aws-resources/load-resource/aws_instance`<br>`POST /api/v1/request/aws-resources/save-resource/aws_instance` |
| `aws_subnet` | `POST /api/v1/request/aws-resources/delete-draft/aws_subnet`<br>`POST /api/v1/request/aws-resources/duplicate-draft/aws_subnet`<br>`POST /api/v1/request/aws-resources/exec-tofu-apply/aws_subnet`<br>`POST /api/v1/request/aws-resources/exec-tofu-destroy/aws_subnet`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan-destroy/aws_subnet`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan/aws_subnet`<br>`POST /api/v1/request/aws-resources/issue-unique-id/aws_subnet`<br>`POST /api/v1/request/aws-resources/load-draft-version/aws_subnet`<br>`POST /api/v1/request/aws-resources/load-resource/aws_subnet`<br>`POST /api/v1/request/aws-resources/save-resource/aws_subnet` |
| `aws_security_group` | `POST /api/v1/request/aws-resources/delete-draft/aws_security_group`<br>`POST /api/v1/request/aws-resources/duplicate-draft/aws_security_group`<br>`POST /api/v1/request/aws-resources/exec-tofu-apply/aws_security_group`<br>`POST /api/v1/request/aws-resources/exec-tofu-destroy/aws_security_group`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan-destroy/aws_security_group`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan/aws_security_group`<br>`POST /api/v1/request/aws-resources/issue-unique-id/aws_security_group`<br>`POST /api/v1/request/aws-resources/load-draft-version/aws_security_group`<br>`POST /api/v1/request/aws-resources/load-resource/aws_security_group`<br>`POST /api/v1/request/aws-resources/save-resource/aws_security_group` |
| `aws_internet_gateway` | `POST /api/v1/request/aws-resources/delete-draft/aws_internet_gateway`<br>`POST /api/v1/request/aws-resources/duplicate-draft/aws_internet_gateway`<br>`POST /api/v1/request/aws-resources/exec-tofu-apply/aws_internet_gateway`<br>`POST /api/v1/request/aws-resources/exec-tofu-destroy/aws_internet_gateway`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan-destroy/aws_internet_gateway`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan/aws_internet_gateway`<br>`POST /api/v1/request/aws-resources/issue-unique-id/aws_internet_gateway`<br>`POST /api/v1/request/aws-resources/load-draft-version/aws_internet_gateway`<br>`POST /api/v1/request/aws-resources/load-resource/aws_internet_gateway`<br>`POST /api/v1/request/aws-resources/save-resource/aws_internet_gateway` |
| `aws_route_table` | `POST /api/v1/request/aws-resources/delete-draft/aws_route_table`<br>`POST /api/v1/request/aws-resources/duplicate-draft/aws_route_table`<br>`POST /api/v1/request/aws-resources/exec-tofu-apply/aws_route_table`<br>`POST /api/v1/request/aws-resources/exec-tofu-destroy/aws_route_table`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan-destroy/aws_route_table`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan/aws_route_table`<br>`POST /api/v1/request/aws-resources/issue-unique-id/aws_route_table`<br>`POST /api/v1/request/aws-resources/load-draft-version/aws_route_table`<br>`POST /api/v1/request/aws-resources/load-resource/aws_route_table`<br>`POST /api/v1/request/aws-resources/save-resource/aws_route_table` |
| `aws_route_table_association` | `POST /api/v1/request/aws-resources/delete-draft/aws_route_table_association`<br>`POST /api/v1/request/aws-resources/duplicate-draft/aws_route_table_association`<br>`POST /api/v1/request/aws-resources/exec-tofu-apply/aws_route_table_association`<br>`POST /api/v1/request/aws-resources/exec-tofu-destroy/aws_route_table_association`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan-destroy/aws_route_table_association`<br>`POST /api/v1/request/aws-resources/exec-tofu-plan/aws_route_table_association`<br>`POST /api/v1/request/aws-resources/issue-unique-id/aws_route_table_association`<br>`POST /api/v1/request/aws-resources/load-draft-version/aws_route_table_association`<br>`POST /api/v1/request/aws-resources/load-resource/aws_route_table_association`<br>`POST /api/v1/request/aws-resources/save-resource/aws_route_table_association` |

## 7. 주요 요청 DTO

### 7.1 `ResourceDto`

```java
private Map<String, Object> token;
private String regionCode;
private String authUserIndex;
private String resourceSaveName;
private Map<String, Object> resource;
private Boolean isNextCallable;
private List<Map<String, Object>> draftVersion;
private List<List<Map<String, Object>>> duplicateDraftVersion;
private String uniqueId;
private String operation;
private String moduleName;
```

주요 용도:

- AWS 리소스 저장/조회
- OpenTofu 파일 생성
- OpenTofu 실행 요청
- draft version 처리

### 7.2 `TemporaryCredential`

```java
private String authEmailId;
private String awsAccessKey;
private String awsSecretAccessKey;
private String awsSessionToken;
private Boolean isNextCallable;
private String uuid;
```

주요 용도:

- STS 검증
- AWS Account ID 조회
- 계정 등록 전 임시 자격 증명 처리

### 7.3 `AwsCloudRequest`

```java
private List<Map<String, Object>> selectedAccountObjectList;
private List<String> selectedRegions;
private String selectedScriptTypeValue;
private String uniqueId;
private Boolean isNextCallable;
```

주요 용도:

- 다중 계정/다중 리전 대상 작업
- 다운로드 및 auto clear 작업

## 8. 데이터 모델 개요

JPA Entity는 크게 두 그룹으로 구성됩니다.

### 8.1 AWS 계정

```text
StsEntity
```

AWS 계정 정보와 임시 자격 증명 기반 메타데이터를 저장합니다.

### 8.2 AWS 리소스

```text
VpcEntity
VpcIpamPoolEntity
InstanceEntity
SubnetEntity
SecurityGroupEntity
InternetGatewayEntity
RouteTableEntity
RouteTableAssociationEntity
```

각 리소스는 공통 Base Entity를 상속/활용하며, Security Group과 Instance는 하위 relation entity를 추가로 가집니다.

예:

```text
SecurityGroupEntity
  ├─ IngressCidrBlockGroup
  ├─ IngressIpv6CidrBlockGroup
  ├─ IngressPrefixListIdGroup
  ├─ IngressSecurityGroupGroup
  ├─ EgressCidrBlockGroup
  ├─ EgressIpv6CidrBlockGroup
  ├─ EgressPrefixListIdGroup
  └─ EgressSecurityGroupGroup

InstanceEntity
  ├─ EbsDevTagGroup
  └─ PrivateIpAddressGroup
```

## 9. 보안 구조

관련 파일:

```text
config/SecurityConfig.java
config/FilterConfig.java
servlet/filter/DomainFilter.java
servlet/filter/JwtAccessTokenFilter.java
servlet/filter/CustomCsrfFilter.java
servlet/filter/FinalCorsFixFilter.java
```

구성:

- HTTPS 443 포트 사용
- `keystore.p12` 기반 SSL 설정
- `JSESSIONID` 쿠키 `Secure`, `HttpOnly`, `SameSite=None`
- `jwtAccessToken` 쿠키 기반 JWT 검증
- SSO 서버의 공개키 API에서 RSA 공개키 조회 후 JWT 서명 검증
- Host Header를 `java.opentofu.click`로 제한
- CORS Origin을 `https://studio.opentofu.click`로 제한
- CSRF 토큰 API 제공

주의 사항:

- 현재 `application.properties`에 운영 도메인, DB 접속 정보, SSL keystore 경로가 포함되어 있습니다.
- 실제 운영 저장소에서는 비밀번호, keystore, AWS 자격 증명, DB 계정 정보를 Git에 포함하지 말고 환경 변수 또는 Secret Manager로 분리해야 합니다.
- `server.error.include-stacktrace=always`, `logging.level.root=DEBUG`는 운영 환경에서는 정보 노출 위험이 있으므로 조정이 필요합니다.
- JWT 필터가 공개키 내용을 로그로 출력하는 로직이 있어 운영에서는 제거하는 것이 안전합니다.

## 10. 실행 환경 요구사항

필수 구성:

- JDK 21
- Maven 또는 Maven Wrapper
- MySQL 8.x
- Redis
- Python 3
- boto3 / botocore
- OpenTofu CLI (`tofu` 명령어가 PATH에 있어야 함)
- SSL keystore 또는 로컬 개발용 HTTP 설정

기본 설정 파일:

```text
src/main/resources/application.properties
```

주요 설정:

| 설정 | 현재 값/의미 |
|---|---|
| `server.port` | 443 |
| `spring.datasource.url` | MySQL 접속 URL |
| `spring.redis.host` | Redis Host |
| `sso.publicKeyUrl` | SSO 공개키 조회 URL |
| `spring.jpa.hibernate.ddl-auto` | update |
| `spring.mvc.async.request-timeout` | 3600000ms |
| `logging.file.name` | `/home/ec2-user/log/java-sprout-app.log` |

## 11. 로컬 실행 방법

### 11.1 의존성 준비

```bash
java -version
python3 --version
tofu version
```

Python AWS 조회 기능을 사용하려면 다음 패키지가 필요합니다.

```bash
pip3 install boto3 botocore
```

### 11.2 설정 분리 권장

운영 설정이 포함된 `application.properties`를 그대로 사용하지 말고, 로컬 프로필을 따로 두는 것을 권장합니다.

예:

```bash
SPRING_PROFILES_ACTIVE=local
```

로컬에서는 다음 값들을 별도 설정하는 것이 좋습니다.

```properties
server.port=8080
server.ssl.enabled=false
spring.datasource.url=jdbc:mysql://localhost:3306/awscloud_sprout
spring.datasource.username=...
spring.datasource.password=...
spring.redis.host=localhost
spring.redis.port=6379
```

### 11.3 빌드

```bash
./mvnw clean package
```

### 11.4 테스트

```bash
./mvnw test
```

업로드된 ZIP 내부의 기존 Surefire 결과 기준으로는 `SproutApplicationTests` 1건이 통과한 기록이 있습니다.

### 11.5 실행

```bash
java -jar target/sprout-0.0.1-SNAPSHOT.jar
```

또는:

```bash
./mvnw spring-boot:run
```

## 12. Workspace 경로

코드 상수 기준 기본 경로는 다음과 같습니다.

```text
ROOT_PATH=/home/ec2-user/opentofu/workspace
DOWNLOAD_ROOT_PATH=/home/ec2-user/opentofu/download
```

OpenTofu 작업 디렉터리 예:

```text
/home/ec2-user/opentofu/workspace/{authEmailId}/{accountUuid}/{regionCode}/tofu_module/{moduleName}
/home/ec2-user/opentofu/workspace/{authEmailId}/{accountUuid}/{regionCode}/tofu_resource/{moduleName}
```

## 13. 대표 요청 흐름

### 13.1 AWS 계정 등록 흐름

```text
POST /api/v1/register/temporary-credential
  → TemporaryCredential 수신
  → boto3 STS Python script 생성
  → python3 실행
  → AWS Account ID 조회
  → StsEntity 저장
```

### 13.2 AWS 리소스 저장 흐름

```text
POST /api/v1/request/aws-resources/save-resource/{resource}
  → ResourceDto 수신
  → resource map 파싱
  → EntityBuilder 선택
  → JPA Entity 생성
  → MySQL 저장
```

### 13.3 OpenTofu Plan 흐름

```text
POST /api/v1/request/aws-resources/exec-tofu-plan/{resource}
  → ResourceDto 수신
  → Redis semaphore(tofu:plan) 획득
  → 작업 디렉터리 계산
  → tofu init 실행
  → tofu plan 실행
  → stdout을 SSE로 전송
  → semaphore release
```

### 13.4 OpenTofu Apply 흐름

```text
POST /api/v1/request/aws-resources/exec-tofu-apply/{resource}
  → Redis semaphore(tofu:apply) 획득
  → tofu init
  → tofu apply -auto-approve
  → SSE 로그 전송
```

### 13.5 OpenTofu Destroy 흐름

```text
POST /api/v1/request/aws-resources/exec-tofu-destroy/{resource}
  → Redis semaphore(tofu:destroy) 획득
  → tofu init
  → tofu destroy -auto-approve
  → SSE 로그 전송
```

## 14. 코드 구조

```text
src/main/java/click/opentofu/sprout
├── config              # Spring, Security, Async, Redis 설정
├── constants           # 경로/타임아웃/모듈 상수
├── controller          # REST API Controller
│   ├── csrf
│   ├── sts
│   ├── resource
│   ├── draft
│   └── emitter
├── dto                 # Request/Response DTO
├── handler
│   ├── entity          # Entity Builder, Repository, JPA Entity
│   ├── python          # boto3 Python script builder
│   └── tofu            # OpenTofu resource/module builder
├── redis               # Redisson semaphore 초기화/조회
├── service             # 비즈니스 서비스
├── servlet/filter      # Domain/JWT/CSRF/CORS filter
└── util                # 공통 유틸리티
```

## 15. 운영 관점 체크포인트

현재 코드 기준으로 운영 전 점검해야 할 항목입니다.

| 영역 | 체크포인트 |
|---|---|
| Secret 관리 | DB password, SSL keystore, AWS credential 분리 필요 |
| Logging | DEBUG/stacktrace always 운영 노출 제한 필요 |
| OpenTofu 실행 | `apply -auto-approve`, `destroy -auto-approve`는 승인/권한 정책 필요 |
| 동시성 | Redis semaphore permit이 실제 서버 CPU/메모리/계정 한도와 맞는지 검증 필요 |
| Workspace | 사용자별 디렉터리 정리 정책 필요 |
| Python 실행 | 동적 생성 스크립트의 입력 검증/권한 제한 필요 |
| 보안 | Host/CORS/JWT/CSRF 정책의 운영 도메인 일관성 점검 필요 |
| DB 스키마 | `ddl-auto=update` 운영 사용 여부 검토 필요 |
| 장애 대응 | tofu 실행 실패/중단 시 semaphore release와 workspace cleanup 검증 필요 |

## 16. 현재 코드에서 확인된 테스트 상태

ZIP에 포함된 Surefire report 기준:

```text
Tests run: 1
Failures: 0
Errors: 0
Skipped: 0
```

단, 이 문서는 업로드된 소스와 기존 test report를 분석하여 작성한 것이며, 현재 실행 환경에서 전체 통합 테스트를 새로 수행한 결과는 아닙니다.
