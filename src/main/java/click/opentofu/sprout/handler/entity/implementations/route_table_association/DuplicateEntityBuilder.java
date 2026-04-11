package click.opentofu.sprout.handler.entity.implementations.route_table_association;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.handler.entity.entity.route_table_association.entity.RouteTableAssociationEntity;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.RouteTableAssociationRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("duplicate_entity_builder_route_table_association")
public class DuplicateEntityBuilder implements EntityHandler {
    
    private final RouteTableAssociationRepository routeTableAssociationRepository;

    @Override
    public void duplicateBuildEntityAndSave(JsonNode parameters, String authUserIndex, String accountId, String region, String moduleName) {
        try {
            String createOnlyId = "create-only-" + UUID.randomUUID().toString().replace("-", "").substring(0, 17);

            RouteTableAssociationEntity routeTableAssociationEntity = RouteTableAssociationEntity.builder()
                .resourceSaveName(parameters.path("resource_save_name").asText())
                .tenantId(authUserIndex)

                .routeTableAssociationId(createOnlyId)
                // .routeTableAssociationArn(parameters.path("route_table_association_arn").asText())
                .accountId(accountId)
                .region(region)
                .name(createOnlyId)
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
