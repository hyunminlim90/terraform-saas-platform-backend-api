package click.opentofu.sprout.handler.entity.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import click.opentofu.sprout.handler.entity.entity.vpc.entity.VpcEntity;
import click.opentofu.sprout.handler.entity.interfaces.BaseQueryRepository;

@Repository("vpc_repository")
public interface VpcRepository extends JpaRepository<VpcEntity, String>, BaseQueryRepository<VpcEntity> {}
