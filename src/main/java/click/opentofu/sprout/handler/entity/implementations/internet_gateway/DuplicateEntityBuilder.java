package click.opentofu.sprout.handler.entity.implementations.internet_gateway;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.handler.entity.entity.internet_gateway.entity.InternetGatewayEntity;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.InternetGatewayRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("duplicate_entity_builder_internet_gateway")
public class DuplicateEntityBuilder implements EntityHandler {
    
    private final InternetGatewayRepository internetGatewayRepository;

    @Override
    public void duplicateBuildEntityAndSave(JsonNode parameters, String authUserIndex, String accountId, String region, String moduleName) {
        try {
            String createOnlyId = "create-only-" + UUID.randomUUID().toString().replace("-", "").substring(0, 17);

            InternetGatewayEntity internetGatewayEntity = InternetGatewayEntity.builder()
                .resourceSaveName(parameters.path("resource_save_name").asText())
                .tenantId(authUserIndex)

                .internetGatewayId(createOnlyId)
                // .internetGatewayArn(parameters.path("internet_gateway_arn").asText())
                .accountId(accountId)
                .region(region)
                .name(createOnlyId)
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
