package click.opentofu.sprout.handler.entity.implementations.security_group;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.handler.entity.entity.security_group.entity.SecurityGroupEntity;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.EgressCidrBlockGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.EgressIpv6CidrBlockGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.EgressPrefixListIdGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.EgressSecurityGroupGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.IngressCidrBlockGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.IngressIpv6CidrBlockGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.IngressPrefixListIdGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.IngressSecurityGroupGroup;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.SecurityGroupRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("duplicate_entity_builder_security_group")
public class DuplicateEntityBuilder implements EntityHandler {
    
    private final SecurityGroupRepository securityGroupRepository;

    @Override
    public void duplicateBuildEntityAndSave(JsonNode parameters, String authUserIndex, String accountId, String region, String moduleName) {
        try {
            String createOnlyId = "create-only-" + UUID.randomUUID().toString().replace("-", "").substring(0, 17);

            SecurityGroupEntity securityGroupEntity = SecurityGroupEntity.builder()
                .resourceSaveName(parameters.path("resource_save_name").asText())
                .tenantId(authUserIndex)

                .securityGroupId(createOnlyId)
                // .securityGroupArn(parameters.path("security_group_arn").asText())
                .accountId(accountId)
                .region(region)
                .name(createOnlyId)
                .moduleName(moduleName)

                /** Declarations for DTO-to-Entity Mappings */

                .description(parameters.path("description").asText())
                .securityGroupName(parameters.path("security_group_name").asText())
                .namePrefix(parameters.path("name_prefix").asText())
                .vpcId(parameters.path("vpc_id").asText())
                .revokeRulesOnDelete(parameters.path("revoke_rules_on_delete").asBoolean())
                .ingressProtocol(parseToListString(parameters.path("ingress_protocol")))
                .ingressDescription(parseToListString(parameters.path("ingress_description")))
                .egressProtocol(parseToListString(parameters.path("egress_protocol")))
                .egressDescription(parseToListString(parameters.path("egress_description")))
                .ingressFromPort(parseToListString(parameters.path("ingress_from_port")))
                .ingressToPort(parseToListString(parameters.path("ingress_to_port")))
                .egressFromPort(parseToListString(parameters.path("egress_from_port")))
                .egressToPort(parseToListString(parameters.path("egress_to_port")))
                .ingressSelf(parseToListString(parameters.path("ingress_self")))
                .egressSelf(parseToListString(parameters.path("egress_self")))

                /** Declarations for DTO-to-Entity Mappings */

                .tags(parseTags(parameters.path("tags")))
                .build();

            /** Declarations of Join Entity Converter Methods */

            List<IngressCidrBlockGroup> ingressCidrBlocks = parseToIngressCidrBlockGroupList(parameters.path("ingress_cidr_blocks"), securityGroupEntity);
            securityGroupEntity.setIngressCidrBlocks(ingressCidrBlocks);

            List<IngressIpv6CidrBlockGroup> ingressIpv6CidrBlocks = parseToIngressIpv6CidrBlockGroupList(parameters.path("ingress_ipv6_cidr_blocks"), securityGroupEntity);
            securityGroupEntity.setIngressIpv6CidrBlocks(ingressIpv6CidrBlocks);

            List<IngressPrefixListIdGroup> ingressPrefixListIds = parseToIngressPrefixListIdGroupList(parameters.path("ingress_prefix_list_ids"), securityGroupEntity);
            securityGroupEntity.setIngressPrefixListIds(ingressPrefixListIds);

            List<IngressSecurityGroupGroup> ingressSecurityGroups = parseToIngressSecurityGroupGroupList(parameters.path("ingress_security_groups"), securityGroupEntity);
            securityGroupEntity.setIngressSecurityGroups(ingressSecurityGroups);

            List<EgressCidrBlockGroup> egressCidrBlocks = parseToEgressCidrBlockGroupList(parameters.path("egress_cidr_blocks"), securityGroupEntity);
            securityGroupEntity.setEgressCidrBlocks(egressCidrBlocks);

            List<EgressIpv6CidrBlockGroup> egressIpv6CidrBlocks = parseToEgressIpv6CidrBlockGroupList(parameters.path("egress_ipv6_cidr_blocks"), securityGroupEntity);
            securityGroupEntity.setEgressIpv6CidrBlocks(egressIpv6CidrBlocks);

            List<EgressPrefixListIdGroup> egressPrefixListIds = parseToEgressPrefixListIdGroupList(parameters.path("egress_prefix_list_ids"), securityGroupEntity);
            securityGroupEntity.setEgressPrefixListIds(egressPrefixListIds);

            List<EgressSecurityGroupGroup> egressSecurityGroups = parseToEgressSecurityGroupGroupList(parameters.path("egress_security_groups"), securityGroupEntity);
            securityGroupEntity.setEgressSecurityGroups(egressSecurityGroups);
            
            /** Declarations of Join Entity Converter Methods */

            securityGroupRepository.save(securityGroupEntity);
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
