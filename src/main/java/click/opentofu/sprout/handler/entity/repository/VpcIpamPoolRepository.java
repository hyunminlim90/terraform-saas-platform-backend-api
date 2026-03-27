package click.opentofu.sprout.handler.entity.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import click.opentofu.sprout.handler.entity.entity.vpc_ipam_pool.entity.VpcIpamPoolEntity;
import click.opentofu.sprout.handler.entity.interfaces.BaseQueryRepository;

@Repository("vpc_ipam_pool_repository")
public interface VpcIpamPoolRepository extends JpaRepository<VpcIpamPoolEntity, String>, BaseQueryRepository<VpcIpamPoolEntity> {}
