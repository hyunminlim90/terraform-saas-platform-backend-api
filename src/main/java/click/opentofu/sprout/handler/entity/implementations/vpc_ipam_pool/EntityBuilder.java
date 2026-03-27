package click.opentofu.sprout.handler.entity.implementations.vpc_ipam_pool;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.handler.entity.entity.vpc_ipam_pool.entity.VpcIpamPoolEntity;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.VpcIpamPoolRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("entity_builder_vpc_ipam_pool")
public class EntityBuilder implements EntityHandler {
    
    private final VpcIpamPoolRepository vpcIpamPoolRepository;

    @Override
    public void buildEntityAndSave(JsonNode parameters, String resourceSaveName, String authUserIndex, String moduleName) {
        try {
            VpcIpamPoolEntity tofuEntity = VpcIpamPoolEntity.builder()
                .resourceSaveName(resourceSaveName)
                .tenantId(authUserIndex)

                .vpcIpamPoolId(parameters.path("vpc_ipam_pool_id").asText())
                .vpcIpamPoolArn(parameters.path("vpc_ipam_pool_arn").asText())
                .accountId(parameters.path("account_id").asText())
                .region(parameters.path("region").asText())
                .name(parameters.path("vpc_ipam_pool_id").asText())
                .moduleName(moduleName)

                /** Declarations for DTO-to-Entity Mappings */

                .addressFamily(parameters.path("address_family").asText())
                .awsService(parameters.path("aws_service").asText())
                .description(parameters.path("description").asText())
                .ipamScopeId(parameters.path("ipam_scope_id").asText())
                .locale(parameters.path("locale").asText())
                .publicIpSource(parameters.path("public_ip_source").asText())
                .sourceIpamPoolId(parameters.path("source_ipam_pool_id").asText())
                .allocationDefaultNetmaskLength(parameters.path("allocation_default_netmask_length").asText())
                .allocationMaxNetmaskLength(parameters.path("allocation_max_netmask_length").asText())
                .allocationMinNetmaskLength(parameters.path("allocation_min_netmask_length").asText())
                .autoImport(parameters.path("auto_import").asBoolean())
                .cascade(parameters.path("cascade").asBoolean())
                .publiclyAdvertisable(parameters.path("publicly_advertisable").asBoolean())
                .allocationResourceTags(parseAllocationResourceTags(parameters.path("allocation_resource_tags"))) 

                /** Declarations for DTO-to-Entity Mappings */

                .tags(parseTags(parameters.path("tags")))
                .build();

            /** Declarations of Join Entity Converter Methods */

            


            /** Declarations of Join Entity Converter Methods */

            vpcIpamPoolRepository.save(tofuEntity);
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
