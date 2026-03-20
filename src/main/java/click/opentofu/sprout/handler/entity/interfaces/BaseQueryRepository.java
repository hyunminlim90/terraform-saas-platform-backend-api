package click.opentofu.sprout.handler.entity.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BaseQueryRepository<TofuEntity> {

    @Query("SELECT t FROM #{#entityName} t WHERE t.tenantId = :tenantId AND t.accountId = :accountId AND t.region = :region")
    List<TofuEntity> findByTenantIdAndAccountIdAndRegion(
        @Param("tenantId") String tenantId,
        @Param("accountId") String accountId,
        @Param("region") String region
    );

    List<TofuEntity> findByTenantIdAndAccountIdAndRegionAndResourceSaveName(
        String tenantId,
        String accountId,
        String region,
        String resourceSaveName
    );

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM #{#entityName} e WHERE e.tenantId = :tenantId AND e.accountId = :accountId AND e.region = :region AND e.resourceSaveName = :resourceSaveName")
    boolean existsByTenantIdAndAccountIdAndRegionAndResourceSaveName(
        @Param("tenantId") String tenantId,
        @Param("accountId") String accountId,
        @Param("region") String region,
        @Param("resourceSaveName") String resourceSaveName
    );

    @Query("SELECT DISTINCT e.resourceSaveName FROM #{#entityName} e WHERE e.tenantId = :tenantId AND e.accountId = :accountId AND e.region = :region AND e.resourceSaveName IN :resourceSaveNames")
    List<String> findExistingResourceSaveNames(
        @Param("tenantId") String tenantId,
        @Param("accountId") String accountId,
        @Param("region") String region,
        @Param("resourceSaveNames") List<String> resourceSaveNames
    );

    void delete(TofuEntity entity);
}
