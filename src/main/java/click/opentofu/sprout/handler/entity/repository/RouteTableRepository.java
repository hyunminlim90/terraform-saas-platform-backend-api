package click.opentofu.sprout.handler.entity.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import click.opentofu.sprout.handler.entity.entity.route_table.entity.RouteTableEntity;
import click.opentofu.sprout.handler.entity.interfaces.BaseQueryRepository;

@Repository("route_table_repository")
public interface RouteTableRepository extends JpaRepository<RouteTableEntity, String>, BaseQueryRepository<RouteTableEntity> {}
