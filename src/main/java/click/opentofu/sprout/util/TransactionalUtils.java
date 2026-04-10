package click.opentofu.sprout.util;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import click.opentofu.sprout.dto.interfaces.ModuleDto;
import click.opentofu.sprout.dto.response.InstanceDto;
import click.opentofu.sprout.dto.response.InternetGatewayDto;
import click.opentofu.sprout.dto.response.SecurityGroupDto;
import click.opentofu.sprout.dto.response.SubnetDto;
import click.opentofu.sprout.dto.response.VpcDto;
import click.opentofu.sprout.dto.response.VpcIpamPoolDto;
import click.opentofu.sprout.handler.entity.entity.instance.entity.InstanceEntity;
import click.opentofu.sprout.handler.entity.entity.internet_gateway.entity.InternetGatewayEntity;
import click.opentofu.sprout.handler.entity.entity.security_group.entity.SecurityGroupEntity;
import click.opentofu.sprout.handler.entity.entity.subnet.entity.SubnetEntity;
import click.opentofu.sprout.handler.entity.entity.vpc.entity.VpcEntity;
import click.opentofu.sprout.handler.entity.entity.vpc_ipam_pool.entity.VpcIpamPoolEntity;
import click.opentofu.sprout.handler.entity.interfaces.BaseQueryRepository;

@Component
public class TransactionalUtils {
    
    @Transactional
    public <T> void deleteResourceSaveName(
        BaseQueryRepository<T> baseQueryRepository,
        String tenantId,
        String accountId,
        String region,
        String resourceSaveName,
        String moduleName
    ) {
        List<T> entities = baseQueryRepository.findByTenantIdAndAccountIdAndRegionAndResourceSaveNameAndModuleName(tenantId, accountId, region, resourceSaveName, moduleName);
        for (T entity : entities) {
            baseQueryRepository.delete(entity);
        }
    }

    @Transactional
    public List<ModuleDto> fetchWithLazyInit(
        JpaRepository<?, ?> repository,
        String tenantId,
        String accountId,
        String region,
        String moduleName
    ) {
        if (!(repository instanceof BaseQueryRepository)) {
            throw new RuntimeException("load_draft_version_repository_instanceof_base_query_repository");
        }

        BaseQueryRepository<?> baseQueryRepository = (BaseQueryRepository<?>) repository;

        List<?> result = baseQueryRepository.findByTenantIdAndAccountIdAndRegionAndModuleName(tenantId, accountId, region, moduleName);
       
        /** Hibernate의 LAZY 로딩을 강제로 초기화 */

        result.forEach(e -> {

            switch (moduleName) {

                case "aws_vpc" -> {}
                case "aws_vpc_ipam_pool" -> {}
                case "aws_instance" -> {
                    InstanceEntity entity = (InstanceEntity) e;

                    Hibernate.initialize(entity.getEbsDevTags());
                    Hibernate.initialize(entity.getPrivateIpAddresses());
                }
                case "aws_subnet" -> {}
                case "aws_security_group" -> {
                    SecurityGroupEntity entity = (SecurityGroupEntity) e;
            
                    Hibernate.initialize(entity.getEgressCidrBlocks());
                    Hibernate.initialize(entity.getEgressIpv6CidrBlocks());
                    Hibernate.initialize(entity.getEgressPrefixListIds());
                    Hibernate.initialize(entity.getEgressSecurityGroups());
                    Hibernate.initialize(entity.getIngressCidrBlocks());
                    Hibernate.initialize(entity.getIngressIpv6CidrBlocks());
                    Hibernate.initialize(entity.getIngressPrefixListIds());
                    Hibernate.initialize(entity.getIngressSecurityGroups());
                }
                case "aws_internet_gateway" -> {}
                
                default -> {}
            }
        });

        /** DTO 변환 로직 */

        return result.stream()
            .map(e -> {

                return (ModuleDto) switch (moduleName) {

                    case "aws_vpc" -> VpcDto.from((VpcEntity) e);
                    case "aws_vpc_ipam_pool" -> VpcIpamPoolDto.from((VpcIpamPoolEntity) e);
                    case "aws_instance" -> InstanceDto.from((InstanceEntity) e);
                    case "aws_subnet" -> SubnetDto.from((SubnetEntity) e);
                    case "aws_security_group" -> SecurityGroupDto.from((SecurityGroupEntity) e);
                    case "aws_internet_gateway" -> InternetGatewayDto.from((InternetGatewayEntity) e);

                    default -> throw new RuntimeException("transactional_utils_fetch_with_lazy_init_switch_default");
                };
            })
            .toList();
    }
}
