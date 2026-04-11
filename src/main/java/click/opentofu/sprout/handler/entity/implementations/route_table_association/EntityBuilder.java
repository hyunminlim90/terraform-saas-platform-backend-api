package click.opentofu.sprout.handler.entity.implementations.route_table_association;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.handler.entity.entity.route_table_association.entity.RouteTableAssociationEntity;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.RouteTableAssociationRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("entity_builder_route_table_association")
public class EntityBuilder implements EntityHandler {
    
    private final RouteTableAssociationRepository routeTableAssociationRepository;

    @Override
    public void buildEntityAndSave(JsonNode parameters, String resourceSaveName, String authUserIndex, String moduleName) {
        try {
            RouteTableAssociationEntity routeTableAssociationEntity = RouteTableAssociationEntity.builder()
                .resourceSaveName(resourceSaveName)
                .tenantId(authUserIndex)

                .routeTableAssociationId(parameters.path("route_table_association_id").asText())
                // .routeTableAssociationArn(parameters.path("route_table_association_arn").asText())
                .accountId(parameters.path("account_id").asText())
                .region(parameters.path("region").asText())
                .name(parameters.path("route_table_association_id").asText())
                .moduleName(moduleName)

                /** Declarations for DTO-to-Entity Mappings */

                .gatewayId(parameters.path("gateway_id").asText())
                .routeTableId(parameters.path("route_table_id").asText())
                .subnetId(parameters.path("subnet_id").asText())

                /** Declarations for DTO-to-Entity Mappings */

                .build();

            /** Declarations of Join Entity Converter Methods */

            /** Declarations of Join Entity Converter Methods */

            routeTableAssociationRepository.save(routeTableAssociationEntity);
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
