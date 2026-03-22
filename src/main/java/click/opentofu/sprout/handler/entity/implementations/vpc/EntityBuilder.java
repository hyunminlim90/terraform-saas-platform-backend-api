package click.opentofu.sprout.handler.entity.implementations.vpc;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.handler.entity.entity.vpc.entity.VpcEntity;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.VpcRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("entity_builder_vpc")
public class EntityBuilder implements EntityHandler {
    
    private final VpcRepository vpcRepository;

    @Override
    public void buildEntityAndSave(JsonNode parameters, String resourceSaveName, String authUserIndex, String moduleName) {
        try {
            VpcEntity tofuEntity = VpcEntity.builder()
                .resourceSaveName(resourceSaveName)
                .tenantId(authUserIndex)

                .vpcId(parameters.path("vpc_id").asText())
                .accountId(parameters.path("account_id").asText())
                .region(parameters.path("region").asText())
                .name(parameters.path("vpc_id").asText())
                .moduleName(moduleName)

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

                .tags(parseTags(parameters.path("tags")))
                .build();

            vpcRepository.save(tofuEntity);
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
