package click.opentofu.sprout.handler.entity.implementations.internet_gateway;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.handler.entity.entity.internet_gateway.entity.InternetGatewayEntity;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.InternetGatewayRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("entity_builder_internet_gateway")
public class EntityBuilder implements EntityHandler {
    
    private final InternetGatewayRepository internetGatewayRepository;

    @Override
    public void buildEntityAndSave(JsonNode parameters, String resourceSaveName, String authUserIndex, String moduleName) {
        try {
            InternetGatewayEntity internetGatewayEntity = InternetGatewayEntity.builder()
                .resourceSaveName(resourceSaveName)
                .tenantId(authUserIndex)

                .internetGatewayId(parameters.path("internet_gateway_id").asText())
                // .internetGatewayArn(parameters.path("internet_gateway_arn").asText())
                .accountId(parameters.path("account_id").asText())
                .region(parameters.path("region").asText())
                .name(parameters.path("internet_gateway_id").asText())
                .moduleName(moduleName)

                /** Declarations for DTO-to-Entity Mappings */

                .vpcId(parameters.path("vpc_id").asText())

                /** Declarations for DTO-to-Entity Mappings */

                .tags(parseTags(parameters.path("tags")))
                .build();

            /** Declarations of Join Entity Converter Methods */

            /** Declarations of Join Entity Converter Methods */

            internetGatewayRepository.save(internetGatewayEntity);
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
