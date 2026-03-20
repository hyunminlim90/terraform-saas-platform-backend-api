package click.opentofu.sprout.util;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import click.opentofu.sprout.dto.TofuDto;
import click.opentofu.sprout.handler.entity.entity.TofuEntity;
import click.opentofu.sprout.handler.entity.interfaces.BaseQueryRepository;

@Component
public class TransactionalUtils {
    
    @Transactional
    public <T> void deleteResourceSaveName(
        BaseQueryRepository<T> baseQueryRepository,
        String tenantId,
        String accountId,
        String region,
        String resourceSaveName
    ) {
        List<T> entities = baseQueryRepository.findByTenantIdAndAccountIdAndRegionAndResourceSaveName(tenantId, accountId, region, resourceSaveName);
        for (T entity : entities) {
            baseQueryRepository.delete(entity);
        }
    }

    @Transactional
    public List<TofuDto> fetchWithLazyInit(
        JpaRepository<?, ?> repository,
        String tenantId,
        String accountId,
        String region
    ) {
        if (!(repository instanceof BaseQueryRepository)) {
            throw new RuntimeException("load_draft_version_repository_instanceof_base_query_repository");
        }

        BaseQueryRepository<?> baseQueryRepository = (BaseQueryRepository<?>) repository;
        @SuppressWarnings("unchecked")
        List<TofuEntity> result = (List<TofuEntity>) baseQueryRepository.findByTenantIdAndAccountIdAndRegion(tenantId, accountId, region);
       
        /** Hibernate의 LAZY 로딩을 강제로 초기화 */

        result.forEach(e -> {

            // Hibernate.initialize(e.get());
            // Hibernate.initialize(e.get());
            // Hibernate.initialize(e.get());

            Hibernate.initialize(e.getLabels());
            Hibernate.initialize(e.getDriverOpts());
        });

        return result.stream()
            .map(TofuDto::from)
            .toList();
    }
}
