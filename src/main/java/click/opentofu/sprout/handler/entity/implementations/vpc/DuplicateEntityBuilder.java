package click.opentofu.sprout.handler.entity.implementations.vpc;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.handler.entity.entity.vpc.entity.VpcEntity;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.VpcRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("duplicate_entity_builder_vpc")
public class DuplicateEntityBuilder implements EntityHandler {
    
    private final VpcRepository vpcRepository;

    @Override
    public void duplicateBuildEntityAndSave(JsonNode parameters, String authUserIndex, String accountId, String region, String moduleName) {
        try {
            String createOnlyId = "create-only-" + UUID.randomUUID().toString().replace("-", "").substring(0, 17);

            VpcEntity vpcEntity = VpcEntity.builder()
                .resourceSaveName(parameters.path("resource_save_name").asText())
                .tenantId(authUserIndex)

                .vpcId(createOnlyId)
                // .vpcArn(parameters.path("vpc_arn").asText())
                .accountId(accountId)
                .region(region)
                .name(createOnlyId)
                .moduleName(moduleName)

                /** Declarations for DTO-to-Entity Mappings */

                .assignGeneratedIpv6CidrBlock(parameters.path("assign_generated_ipv6_cidr_block").asBoolean())
                .cidrBlock(parameters.path("cidr_block").asText())
                .enableDnsHostnames(parameters.path("enable_dns_hostnames").asBoolean())
                .enableDnsSupport(parameters.path("enable_dns_support").asBoolean())
                .enableNetworkAddressUsageMetrics(parameters.path("enable_network_address_usage_metrics").asBoolean())
                .instanceTenancy(parameters.path("instance_tenancy").asText())
                .ipv4IpamPoolId(parameters.path("ipv4_ipam_pool_id").asText())
                .ipv4NetmaskLength(parameters.path("ipv4_netmask_length").asText())
                .ipv6CidrBlock(parameters.path("ipv6_cidr_block").asText())
                .ipv6CidrBlockNetworkBorderGroup(parameters.path("ipv6_cidr_block_network_border_group").asText())
                .ipv6IpamPoolId(parameters.path("ipv6_ipam_pool_id").asText())
                .ipv6NetmaskLength(parameters.path("ipv6_netmask_length").asText())

                /** Declarations for DTO-to-Entity Mappings */

                .tags(parseTags(parameters.path("tags")))
                .build();

            /** Declarations of Join Entity Converter Methods */


            

            /** Declarations of Join Entity Converter Methods */

            vpcRepository.save(vpcEntity);
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
