package click.opentofu.sprout.handler.entity.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import click.opentofu.sprout.handler.entity.entity.route_table_association.entity.RouteTableAssociationEntity;
import click.opentofu.sprout.handler.entity.interfaces.BaseQueryRepository;

@Repository("route_table_association_repository")
public interface RouteTableAssociationRepository extends JpaRepository<RouteTableAssociationEntity, String>, BaseQueryRepository<RouteTableAssociationEntity> {}
