package click.opentofu.sprout.handler.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import click.opentofu.sprout.handler.entity.entity.sts.entity.StsEntity;

public interface StsRepository extends JpaRepository<StsEntity, String> {
    List<StsEntity> findByAuthEmailId(String authEmailId);

    @Transactional
    @Modifying
    @Query("DELETE FROM sts_entity e WHERE e.accountId LIKE 'ExpiredToken%' OR e.accountId LIKE 'InvalidClientTokenId%'")
    void deleteByAccountIdPrefix();
    void deleteByAccountId(String accountId);
}
