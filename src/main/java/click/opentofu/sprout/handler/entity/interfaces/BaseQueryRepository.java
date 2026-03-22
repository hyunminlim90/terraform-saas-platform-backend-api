package click.opentofu.sprout.handler.entity.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BaseQueryRepository<TofuEntity> {

    @Query("SELECT t FROM #{#entityName} t WHERE t.tenantId = :tenantId AND t.accountId = :accountId AND t.region = :region AND t.moduleName = :moduleName")
    List<TofuEntity> findByTenantIdAndAccountIdAndRegionAndModuleName(
        @Param("tenantId") String tenantId,
        @Param("accountId") String accountId,
        @Param("region") String region,
        @Param("moduleName") String moduleName
    );

    List<TofuEntity> findByTenantIdAndAccountIdAndRegionAndResourceSaveNameAndModuleName(
        String tenantId,
        String accountId,
        String region,
        String resourceSaveName,
        String moduleName
    );

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM #{#entityName} e WHERE e.tenantId = :tenantId AND e.accountId = :accountId AND e.region = :region AND e.resourceSaveName = :resourceSaveName AND e.moduleName = :moduleName")
    boolean existsByTenantIdAndAccountIdAndRegionAndResourceSaveNameAndModuleName(
        @Param("tenantId") String tenantId,
        @Param("accountId") String accountId,
        @Param("region") String region,
        @Param("resourceSaveName") String resourceSaveName,
        @Param("moduleName") String moduleName
    );

    @Query("SELECT DISTINCT e.resourceSaveName FROM #{#entityName} e WHERE e.tenantId = :tenantId AND e.accountId = :accountId AND e.region = :region AND e.moduleName = :moduleName AND e.resourceSaveName IN :resourceSaveNames")
    List<String> findExistingResourceSaveNames(
        @Param("tenantId") String tenantId,
        @Param("accountId") String accountId,
        @Param("region") String region,
        @Param("moduleName") String moduleName,
        @Param("resourceSaveNames") List<String> resourceSaveNames
    );

    void delete(TofuEntity entity);
}
