=begin







        

            

            



            ✝︎ (implementations package) ⇢ tofu_module

                📁 handler
                    ⤷ 📁 tofu
                        ⤷ 📁 module
                            ⤷ 📁 implementations
                                ⤷ 📄 TofuModule.class

                ✨ 'index_key'에서 unique value를 지정한다.
                ✨ yaml hcl 변환부를 지정한다.

                » (Yaml 필드 매핑) ⇢ 타입 종류대로 지정 속력 개선 ⭐

                    (name)

                        ✨ name 필드는 index_key와 동일한 엔티티로 인식하는데 사용한다.
                        
                    (string boolean number)

                        ✝︎ (마킹 값 치환)

                            {{__스네이크케이스_파라미터명__}}

                        ✝︎ (매핑 코드)

                            definedPythonFile
                                .append("    {{__스네이크케이스_파라미터명__}}:\n");
                                    for (Map<String, Object> resourceConfig : resourceConfigs) {
                                        try {
                                            definedPythonFile
                                                .append("        - \"" + resourceConfig.get("{{__스네이크케이스_파라미터명__}}") + "\"\n");
                                        } catch (Exception error) {
                                            throw new RuntimeException("build_tofu_module_config_file_{{__스네이크케이스_파라미터명__}}");
                                        }
                                    }

                    » (textarea)

                        ✝︎ (shell script)

                            (마킹 값 치환)

                                {{__스네이크케이스_파라미터명__}}
                                {{__카멜케이스_파라미터명__}}
                                {{__카멜케이스_AWS제외__}}
                                sprout

                            (매핑 코드)

                                definedPythonFile
                                    .append("    {{__스네이크케이스_파라미터명__}}:\n");
                                        for (Map<String, Object> resourceConfig : resourceConfigs) {
                                            try {
                                                String {{__카멜케이스_파라미터명__}} = (String) resourceConfig.get("{{__스네이크케이스_파라미터명__}}");
                                                if ({{__카멜케이스_파라미터명__}} == null || {{__카멜케이스_파라미터명__}}.isEmpty()) {
                                                    definedPythonFile
                                                        .append("    - \"\"\n");
                                                } else {
                                                    String {{__카멜케이스_AWS제외__}}Id = (String) resourceConfig.get("sprout_id");
                                                    if (!{{__카멜케이스_AWS제외__}}Id.contains("create-only-")) {
                                                        if ({{__카멜케이스_AWS제외__}}Id.startsWith("arn:")) {
                                                            String resource = {{__카멜케이스_AWS제외__}}Id.split(":", 6)[5];
                                                            String[] tokens = resource.split("[/:]");
                                                            {{__카멜케이스_AWS제외__}}Id = String.join("-", tokens);
                                                        }
                                                    }
                                                    definedPythonFile
                                                        .append("    - \"" + {{__카멜케이스_AWS제외__}}Id + "_{{__카멜케이스_파라미터명__}}.sh\"\n");
                                                }
                                            } catch (Exception error) {
                                                throw new RuntimeException("build_tofu_module_config_file_{{__스네이크케이스_파라미터명__}}");
                                            }
                                        }

                        ✝︎ (Json)

                            (마킹 값 치환)

                                {{__스네이크케이스_파라미터명__}}
                                sprout
                                {{__카멜케이스_파라미터명__}}
                                {{__카멜케이스_AWS제외__}}

                            (매핑 코드)

                                definedPythonFile
                                    .append("    {{__스네이크케이스_파라미터명__}}:\n");
                                        for (Map<String, Object> resourceConfig : resourceConfigs) {
                                            try {
                                                boolean isEmpty = false;
                                                Object value = resourceConfig.get("{{__스네이크케이스_파라미터명__}}");
                                                
                                                if (value == null) { isEmpty = true; }
                                                
                                                else if (value instanceof String) {
                                                    ObjectMapper mapper = new ObjectMapper();
                                                    JsonNode node = mapper.readTree((String) value);
                                                    if ((node.isArray() || node.isObject()) && node.size() == 0) { isEmpty = true; }
                                                
                                                } else if (value instanceof List) {
                                                    if (((List<?>) value).isEmpty()) { isEmpty = true; }
                                                
                                                } else if (value instanceof Map) {
                                                    if (((Map<?, ?>) value).isEmpty()) { isEmpty = true; }
                                                }
                                                    
                                                if (isEmpty) {
                                                    definedPythonFile
                                                        .append("        - \"\"\n");
                                                } else {
                                                    String {{__카멜케이스_AWS제외__}}Id = (String) resourceConfig.get("sprout_id");
                                                    if (!{{__카멜케이스_AWS제외__}}Id.contains("create-only-")) {
                                                        if ({{__카멜케이스_AWS제외__}}Id.startsWith("arn:")) {
                                                            String resource = {{__카멜케이스_AWS제외__}}Id.split(":", 6)[5];
                                                            String[] tokens = resource.split("[/:]");
                                                            {{__카멜케이스_AWS제외__}}Id = String.join("-", tokens);
                                                        }
                                                    }
                                                    definedPythonFile
                                                        .append("        - \"" + {{__카멜케이스_AWS제외__}}Id + "_{{__카멜케이스_파라미터명__}}.json\"\n");
                                                }
                                            } catch (Exception error) {
                                                throw new RuntimeException("build_tofu_module_config_file_{{__스네이크케이스_파라미터명__}}");
                                            }
                                        }

                    (string[] boolean[] number[])

                        ✝︎ (마킹 값 치환)

                            {{__스네이크케이스_파라미터명__}}
                            {{__카멜케이스_파라미터명__}}

                        ✝︎ (매핑 코드)

                            definedPythonFile
                                .append("    {{__스네이크케이스_파라미터명__}}:\n");
                                    for (Map<String, Object> resourceConfig : resourceConfigs) {
                                        try {
                                            @SuppressWarnings("unchecked")
                                            List<Object> {{__카멜케이스_파라미터명__}} = (List<Object>) resourceConfig.get("{{__스네이크케이스_파라미터명__}}");
                                            definedPythonFile
                                                .append("        - \"" + String.join("{{__,__}}", {{__카멜케이스_파라미터명__}}.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                                        } catch (Exception error) {
                                            throw new RuntimeException("build_tofu_module_config_file_{{__스네이크케이스_파라미터명__}}");
                                        }
                                    }

                    (object)

                        ✝︎ (마킹 값 치환)

                            {{__스네이크케이스_파라미터명__}}
                            {{__카멜케이스_파라미터명__}}
                            {{__카멜케이스_파라미터명_단수__}}
                            {{__파스칼케이스_단수_띄어쓰기__}}

                        ✝︎ (매핑 코드)

                            definedPythonFile
                                .append("    {{__스네이크케이스_파라미터명__}}:\n");
                                    for (Map<String, Object> resourceConfig : resourceConfigs) {
                                        try {
                                            @SuppressWarnings("unchecked")
                                            Map<String, Object> {{__카멜케이스_파라미터명__}} = (Map<String, Object>) resourceConfig.get("{{__스네이크케이스_파라미터명__}}");
                                            if ({{__카멜케이스_파라미터명__}} != null && !{{__카멜케이스_파라미터명__}}.isEmpty()) {
                                                String {{__카멜케이스_파라미터명_단수__}}Result = {{__카멜케이스_파라미터명__}}.entrySet().stream() 
                                                    .map((entry) -> { return entry.getKey() + "{{__:__}}" + entry.getValue(); })
                                                    .collect(Collectors.joining("{{__,__}}"));
                                                definedPythonFile.append("        - \"" + {{__카멜케이스_파라미터명_단수__}}Result + "\"\n");
                                            } else {
                                                definedPythonFile
                                                    .append("        - \"\"\n");
                                                log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                                                log.warn("{{__파스칼케이스_단수_띄어쓰기__}} does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                                                log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                                            }
                                        } catch (Exception error) {
                                            throw new RuntimeException("build_tofu_module_config_file_{{__스네이크케이스_파라미터명__}}");
                                        }
                                    }

                    (object[])

                        ✝︎ (마킹 값 치환)

                            {{__스네이크케이스_파라미터명__}}
                            {{__카멜케이스_파라미터명__}}
                            {{__카멜케이스_파라미터명_단수__}}
                            {{__파스칼케이스_단수_띄어쓰기__}}

                        ✝︎ (매핑 코드)

                            definedPythonFile
                                .append("    {{__스네이크케이스_파라미터명__}}:\n");
                                    for (Map<String, Object> resourceConfig : resourceConfigs) {
                                        try {
                                            @SuppressWarnings("unchecked")
                                            List<Map<String, Object>> {{__카멜케이스_파라미터명__}} = (List<Map<String, Object>>) resourceConfig.get("{{__스네이크케이스_파라미터명__}}");
                                            if ({{__카멜케이스_파라미터명__}} != null && !{{__카멜케이스_파라미터명__}}.isEmpty()) {
                                                List<String> {{__카멜케이스_파라미터명_단수__}}Strings = new ArrayList<>();
                                                for (Map<String, Object> item : {{__카멜케이스_파라미터명__}}) {
                                                    List<String> keyValuePairs = new ArrayList<>();
                                                    for (Map.Entry<String, Object> entry : item.entrySet()) {
                                                        keyValuePairs.add(entry.getKey() + "{{__:__}}" + entry.getValue());
                                                    }
                                                    {{__카멜케이스_파라미터명_단수__}}Strings.add(String.join("{{__,__}}", keyValuePairs));
                                                }
                                                definedPythonFile
                                                    .append("        - \"")
                                                    .append(String.join("{{__/__}}", {{__카멜케이스_파라미터명_단수__}}Strings))
                                                    .append("\"\n"); 
                                            } else {
                                                definedPythonFile
                                                    .append("        - \"\"\n");
                                                log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                                                log.warn("{{__파스칼케이스_단수_띄어쓰기__}} does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                                                log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                                            }
                                        } catch (Exception error) {
                                            throw new RuntimeException("build_tofu_module_config_file_{{__스네이크케이스_파라미터명__}}");
                                        }
                                    }

                    (<string[]>[])

                        ✝︎ (마킹 값 치환)

                            {{__스네이크케이스_파라미터명__}}
                            {{__카멜케이스_파라미터명__}}
                            {{__카멜케이스_파라미터명_단수__}}
                            {{__파스칼케이스_단수_띄어쓰기__}}

                        ✝︎ (매핑 코드)

                            definedPythonFile
                                .append("    {{__스네이크케이스_파라미터명__}}:\n");
                                    for (Map<String, Object> resourceConfig : resourceConfigs) {
                                        try {
                                            @SuppressWarnings("unchecked")
                                            List<List<String>> {{__카멜케이스_파라미터명__}} = (List<List<String>>) resourceConfig.get("{{__스네이크케이스_파라미터명__}}");
                                            if ({{__카멜케이스_파라미터명__}} != null && !{{__카멜케이스_파라미터명__}}.isEmpty()) {
                                                List<String> {{__카멜케이스_파라미터명_단수__}}Strings = new ArrayList<>();
                                                for (List<String> item : {{__카멜케이스_파라미터명__}}) {
                                                    String joinedInner = String.join("{{__,__}}", item);
                                                    {{__카멜케이스_파라미터명_단수__}}Strings.add(joinedInner);
                                                }
                                                definedPythonFile
                                                    .append("        - \"")
                                                    .append(String.join("{{__/__}}", {{__카멜케이스_파라미터명_단수__}}Strings))
                                                    .append("\"\n"); 
                                            } else {
                                                definedPythonFile
                                                    .append("        - \"\"\n");
                                                log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                                                log.warn("{{__파스칼케이스_단수_띄어쓰기__}} does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                                                log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                                            }
                                        } catch (Exception error) {
                                            throw new RuntimeException("build_tofu_module_config_file_{{__스네이크케이스_파라미터명__}}");
                                        }
                                    }

                ✨ tags 파라미터를 사용하는지 확인한다.
                ✨ yaml 리프 노드를 지정한다.

            ✝︎ (implementations package) ⇢ tofu_resource

                📁 handler
                    ⤷ 📁 tofu
                        ⤷ 📁 resource
                            ⤷ 📁 implementations
                                ⤷ 📄 TofuResource.class

                (backslash 치환)

                    📁 terraform_resource
                        ⤷ 📁 module_name
                            ⤷ 📄 main.tf

                ✨ main.tf 파일을 main.backslash.txt 파일로 복제하고, " 를 \" 로 치환한다.
                ✨ tags 파라미터를 사용하는지 확인한다.
                ✨ lifecycle ignore_changes 를 확인한다.

        » (service package)

            ✝︎ (implementations package)

                📁 service
                    ⤷ 📁 implementations
                        ⤷ 📄 CleanUpDirectory.class
                        ⤷ 📄 DeleteDraftVersion.class
                        ⤷ 📄 DuplicateDraftVersions.class
                        ⤷ 📄 EntitySaveResource.class
                        ⤷ 📄 ExecTofuApply.class
                        ⤷ 📄 ExecTofuDestroy.class
                        ⤷ 📄 ExecTofuPlan.class
                        ⤷ 📄 ExecTofuPlanDestroy.class
                        ⤷ 📄 ExistsDraftVersion.class
                        ⤷ 📄 ExistsResourceSaveName.class
                        ⤷ 📄 IssueUniqueId.class
                        ⤷ 📄 JsonNodeLoadResource.class
                        ⤷ 📄 LoadDraftVersion.class
                        ⤷ 📄 PythonLoadResource.class
                        ⤷ 📄 TofuModule.class
                        ⤷ 📄 TofuResource.class

                » (TofuModule class) 📌 textarea 타입의 경우 해당

                    (파일명 정규화)

                        ✝︎ (주석)

                            /** File extension varies by textarea parameter */

                    ✨ 조건 분기에 {{__카멜케이스_파라미터명__}} 을 지정한다.
                    ✨ 조건 분기에서 파일 확장자를 지정한다.

        » (servlet package)

            ✝︎ (filter package)

                📁 servlet
                    ⤷ 📁 filter
                        ⤷ 📄 CustomCsrfFilter.class
                        ⤷ 📄 DomainFilter.class
                        ⤷ 📄 JwtAccessTokenFilter.class
                
                (DomainFilter class)

                    ✨ ALLOWED_HOST 멤버 변수를 application.properties를 활용하여 수정한다.

        

(나) 
(1) 배포.

    📁 opentofu
        ⤷ 📁 opentofu-backend
            ⤷ 📁 module_name

    ✝︎ (커맨드)

        mvn clean install;
        sudo rsync -avz --no-owner --no-group --progress ./target/mysql-0.0.1-SNAPSHOT.jar sysadmin@java.opentofu.click:/home/sysadmin/deploy/target/;
        rm -rf ./target/mysql-0.0.1-SNAPSHOT.jar;
        
        ls -al ~/.m2/repository/click/opentofu/mysql/0.0.1-SNAPSHOT/mysql-0.0.1-SNAPSHOT.jar;
        rm -rf ~/.m2/repository/click/opentofu/mysql/0.0.1-SNAPSHOT/mysql-0.0.1-SNAPSHOT.jar;
        
        echo "";
        echo "";
        echo "";
        echo "=== jar 파일 검색 ===";
        find ~ -type f -name "*0.0.1-SNAPSHOT.jar" -exec ls -lh {} \; 2>/dev/null;

(다) 
(1) 프로세스 실행.

    📁 home
        ⤷ 📁 sysadmin
            ⤷ 📁 deploy
                ⤷ 📁 target
                ⤷ 📁 nohup-logs

    ✝︎ (커맨드)

        nohup java -jar ~/deploy/target/mysql-0.0.1-SNAPSHOT.jar > ~/deploy/nohup-logs/mysql-output.log 2>&1 &
        nohup java -Djavax.net.ssl.trustStore=/opt/app/certs/truststore.p12 -Djavax.net.ssl.trustStorePassword=changeit -jar ~/deploy/target/mysql-0.0.1-SNAPSHOT.jar > ~/deploy/nohup-logs/mysql-output.log 2>&1 &

        ✨ EC2 에서는 데몬으로 실행한다.

(2) 프로세스 포트 조회.

    ✝︎ (커맨드)
    
        lsof -i :{{__JAVA_포트__}}

        tail -f -n 100 ~/deploy/nohup-logs/mysql-output.log

(3) 테이블 조회.

    ✝︎ (MySQL Workbench)

        ✨ sprout_entify 확인.

(4) sso public key 조회. 📌 서버 마이그레이션의 경우 해당

    ✝︎ (로컬 정적 호스트 매핑)

        📁 etc
            ⤷ 📄 hosts

        » (vm-java)

            (커맨드)

                sudo vi /etc/hosts
            
            (호스트 매핑)

                192.168.35.95       java-sso.opentofu.click








=end
