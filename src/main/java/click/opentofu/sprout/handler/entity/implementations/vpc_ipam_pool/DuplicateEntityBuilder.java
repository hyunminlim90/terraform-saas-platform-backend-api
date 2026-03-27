package click.opentofu.sprout.handler.entity.implementations.vpc_ipam_pool;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.handler.entity.entity.vpc_ipam_pool.entity.VpcIpamPoolEntity;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.VpcIpamPoolRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("duplicate_entity_builder_vpc_ipam_pool")
public class DuplicateEntityBuilder implements EntityHandler {
    
    private final VpcIpamPoolRepository vpcIpamPoolRepository;

    @Override
    public void duplicateBuildEntityAndSave(JsonNode parameters, String authUserIndex, String accountId, String region, String moduleName) {
        try {
            String createOnlyId = "create-only-" + UUID.randomUUID().toString().replace("-", "").substring(0, 17);

            VpcIpamPoolEntity tofuEntity = VpcIpamPoolEntity.builder()
                .resourceSaveName(parameters.path("resource_save_name").asText())
                .tenantId(authUserIndex)

                .vpcIpamPoolId(createOnlyId)
                .vpcIpamPoolArn(parameters.path("vpc_ipam_pool_arn").asText())
                .accountId(accountId)
                .region(region)
                .name(createOnlyId)
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
