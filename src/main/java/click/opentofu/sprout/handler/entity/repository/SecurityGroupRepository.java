package click.opentofu.sprout.handler.entity.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import click.opentofu.sprout.handler.entity.entity.security_group.entity.SecurityGroupEntity;
import click.opentofu.sprout.handler.entity.interfaces.BaseQueryRepository;

@Repository("security_group_repository")
public interface SecurityGroupRepository extends JpaRepository<SecurityGroupEntity, String>, BaseQueryRepository<SecurityGroupEntity> {}
