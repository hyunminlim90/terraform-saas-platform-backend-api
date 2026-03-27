package click.opentofu.sprout.util;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import click.opentofu.sprout.dto.interfaces.ModuleDto;
import click.opentofu.sprout.dto.response.VpcDto;
import click.opentofu.sprout.dto.response.VpcIpamPoolDto;
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

                case "aws_vpc" -> {
                    // VpcEntity entity = (VpcEntity) e;

                    // Hibernate.initialize(entity.get());
                    // Hibernate.initialize(entity.get());
                    // Hibernate.initialize(entity.get());

                    // Hibernate.initialize(entity.getLabels());
                    // Hibernate.initialize(entity.getDriverOpts());
                }

                case "aws_vpc_ipam_pool" -> {}
                
                default -> {}
            }
        });

        return result.stream()
            .map(e -> {

                return (ModuleDto) switch (moduleName) {

                    case "aws_vpc" -> VpcDto.from((VpcEntity) e);
                    case "aws_vpc_ipam_pool" -> VpcIpamPoolDto.from((VpcIpamPoolEntity) e);

                    default -> throw new RuntimeException("transactional_utils_fetch_with_lazy_init_switch_default");
                };
            })
            .toList();
    }
}
