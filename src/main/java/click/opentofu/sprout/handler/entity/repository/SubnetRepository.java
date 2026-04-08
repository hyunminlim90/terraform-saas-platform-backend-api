package click.opentofu.sprout.handler.entity.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import click.opentofu.sprout.handler.entity.entity.subnet.entity.SubnetEntity;
import click.opentofu.sprout.handler.entity.interfaces.BaseQueryRepository;

@Repository("subnet_repository")
public interface SubnetRepository extends JpaRepository<SubnetEntity, String>, BaseQueryRepository<SubnetEntity> {}
