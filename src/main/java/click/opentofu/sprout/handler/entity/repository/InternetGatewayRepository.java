package click.opentofu.sprout.handler.entity.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import click.opentofu.sprout.handler.entity.entity.internet_gateway.entity.InternetGatewayEntity;
import click.opentofu.sprout.handler.entity.interfaces.BaseQueryRepository;

@Repository("internet_gateway_repository")
public interface InternetGatewayRepository extends JpaRepository<InternetGatewayEntity, String>, BaseQueryRepository<InternetGatewayEntity> {}
