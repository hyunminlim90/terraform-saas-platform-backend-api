package click.opentofu.sprout.handler.entity.implementations.route_table;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.handler.entity.entity.route_table.entity.RouteTableEntity;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.RouteTableRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("duplicate_entity_builder_route_table")
public class DuplicateEntityBuilder implements EntityHandler {
    
    private final RouteTableRepository routeTableRepository;

    @Override
    public void duplicateBuildEntityAndSave(JsonNode parameters, String authUserIndex, String accountId, String region, String moduleName) {
        try {
            String createOnlyId = "create-only-" + UUID.randomUUID().toString().replace("-", "").substring(0, 17);

            RouteTableEntity routeTableEntity = RouteTableEntity.builder()
                .resourceSaveName(parameters.path("resource_save_name").asText())
                .tenantId(authUserIndex)

                .routeTableId(createOnlyId)
                // .routeTableArn(parameters.path("route_table_arn").asText())
                .accountId(accountId)
                .region(region)
                .name(createOnlyId)
                .moduleName(moduleName)

                /** Declarations for DTO-to-Entity Mappings */

                .vpcId(parameters.path("vpc_id").asText())
                .propagatingVgws(parseToListString(parameters.path("propagating_vgws")))
                .cidrBlock(parseToListString(parameters.path("cidr_block")))
                .ipv6CidrBlock(parseToListString(parameters.path("ipv6_cidr_block")))
                .destinationPrefixListId(parseToListString(parameters.path("destination_prefix_list_id")))
                .carrierGatewayId(parseToListString(parameters.path("carrier_gateway_id")))
                .coreNetworkArn(parseToListString(parameters.path("core_network_arn")))
                .egressOnlyGatewayId(parseToListString(parameters.path("egress_only_gateway_id")))
                .gatewayId(parseToListString(parameters.path("gateway_id")))
                .localGatewayId(parseToListString(parameters.path("local_gateway_id")))
                .natGatewayId(parseToListString(parameters.path("nat_gateway_id")))
                .networkInterfaceId(parseToListString(parameters.path("network_interface_id")))
                .transitGatewayId(parseToListString(parameters.path("transit_gateway_id")))
                .vpcEndpointId(parseToListString(parameters.path("vpc_endpoint_id")))
                .vpcPeeringConnectionId(parseToListString(parameters.path("vpc_peering_connection_id")))

                /** Declarations for DTO-to-Entity Mappings */

                .tags(parseTags(parameters.path("tags")))
                .build();

            /** Declarations of Join Entity Converter Methods */

            /** Declarations of Join Entity Converter Methods */

            routeTableRepository.save(routeTableEntity);
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
