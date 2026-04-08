package click.opentofu.sprout.handler.entity.implementations.subnet;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.handler.entity.entity.subnet.entity.SubnetEntity;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.SubnetRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("entity_builder_subnet")
public class EntityBuilder implements EntityHandler {
    
    private final SubnetRepository subnetRepository;

    @Override
    public void buildEntityAndSave(JsonNode parameters, String resourceSaveName, String authUserIndex, String moduleName) {
        try {
            SubnetEntity subnetEntity = SubnetEntity.builder()
                .resourceSaveName(resourceSaveName)
                .tenantId(authUserIndex)

                .subnetId(parameters.path("subnet_id").asText())
                // .subnetArn(parameters.path("subnet_arn").asText())
                .accountId(parameters.path("account_id").asText())
                .region(parameters.path("region").asText())
                .name(parameters.path("subnet_id").asText())
                .moduleName(moduleName)

                /** Declarations for DTO-to-Entity Mappings */

                .vpcId(parameters.path("vpc_id").asText())
                .assignIpv6AddressOnCreation(parameters.path("assign_ipv6_address_on_creation").asBoolean())
                .availabilityZone(parameters.path("availability_zone").asText())
                .availabilityZoneId(parameters.path("availability_zone_id").asText())
                .cidrBlock(parameters.path("cidr_block").asText())
                .customerOwnedIpv4Pool(parameters.path("customer_owned_ipv4_pool").asText())
                .enableDns64(parameters.path("enable_dns64").asBoolean())
                .enableLniAtDeviceIndex(parameters.path("enable_lni_at_device_index").asText())
                .enableResourceNameDnsARecordOnLaunch(parameters.path("enable_resource_name_dns_a_record_on_launch").asBoolean())
                .enableResourceNameDnsAaaaRecordOnLaunch(parameters.path("enable_resource_name_dns_aaaa_record_on_launch").asBoolean())
                .ipv6CidrBlock(parameters.path("ipv6_cidr_block").asText())
                .ipv6Native(parameters.path("ipv6_native").asBoolean())
                .mapCustomerOwnedIpOnLaunch(parameters.path("map_customer_owned_ip_on_launch").asBoolean())
                .mapPublicIpOnLaunch(parameters.path("map_public_ip_on_launch").asBoolean())
                .outpostArn(parameters.path("outpost_arn").asText())
                .privateDnsHostnameTypeOnLaunch(parameters.path("private_dns_hostname_type_on_launch").asText())

                /** Declarations for DTO-to-Entity Mappings */

                .tags(parseTags(parameters.path("tags")))
                .build();

            /** Declarations of Join Entity Converter Methods */

            /** Declarations of Join Entity Converter Methods */

            subnetRepository.save(subnetEntity);
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
