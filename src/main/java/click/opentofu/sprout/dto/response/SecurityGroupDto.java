package click.opentofu.sprout.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import click.opentofu.sprout.dto.interfaces.ModuleDto;
import click.opentofu.sprout.handler.entity.entity.security_group.entity.SecurityGroupEntity;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.EgressCidrBlockGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.EgressIpv6CidrBlockGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.EgressPrefixListIdGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.EgressSecurityGroupGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.IngressCidrBlockGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.IngressIpv6CidrBlockGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.IngressPrefixListIdGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.IngressSecurityGroupGroup;

public record SecurityGroupDto (

    /** Parameter section for specifying the fields defined in BaseEntity.class and TofuEntity.class */

    String tenantId,
    String securityGroupId,
    // String securityGroupArn,
    String accountId,
    String region,
    String name,
    String moduleName,

    /** Record component definitions */

    String description,
    String securityGroupName,
    String namePrefix,
    String vpcId,
    Boolean revokeRulesOnDelete,
    List<String> ingressProtocol,
    List<String> ingressDescription,
    List<String> egressProtocol,
    List<String> egressDescription,
    List<String> ingressFromPort,
    List<String> ingressToPort,
    List<String> egressFromPort,
    List<String> egressToPort,
    List<String> ingressSelf,
    List<String> egressSelf,
    List<List<String>> ingressCidrBlocks,
    List<List<String>> ingressIpv6CidrBlocks,
    List<List<String>> ingressPrefixListIds,
    List<List<String>> ingressSecurityGroups,
    List<List<String>> egressCidrBlocks,
    List<List<String>> egressIpv6CidrBlocks,
    List<List<String>> egressPrefixListIds,
    List<List<String>> egressSecurityGroups,

    /** Record component definitions */

    Map<String, String> tags,
    String resourceSaveName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long version

) implements ModuleDto {

    public static SecurityGroupDto from(SecurityGroupEntity e) {
        return new SecurityGroupDto(

            /** Maps a TofuEntity to a TofuDto */

            // e.get
            // e.get
            // e.get

            e.getTenantId(),
            e.getSecurityGroupId(),
            // e.getSecurityGroupArn(),
            e.getAccountId(),
            e.getRegion(),
            e.getName(),
            e.getModuleName(),

            /** JavaBeans getter method declaration */

            e.getDescription(),
            e.getSecurityGroupName(),
            e.getNamePrefix(),
            e.getVpcId(),
            e.getRevokeRulesOnDelete(),
            e.getIngressProtocol(),
            e.getIngressDescription(),
            e.getEgressProtocol(),
            e.getEgressDescription(),
            e.getIngressFromPort(),
            e.getIngressToPort(),
            e.getEgressFromPort(),
            e.getEgressToPort(),
            e.getIngressSelf(),
            e.getEgressSelf(),
            
            e.getIngressCidrBlocks().stream()
                .map(IngressCidrBlockGroup::getIngressCidrBlocks)
                .toList(),
            e.getIngressIpv6CidrBlocks().stream()
                .map(IngressIpv6CidrBlockGroup::getIngressIpv6CidrBlocks)
                .toList(),
            e.getIngressPrefixListIds().stream()
                .map(IngressPrefixListIdGroup::getIngressPrefixListIds)
                .toList(),
            e.getIngressSecurityGroups().stream()
                .map(IngressSecurityGroupGroup::getIngressSecurityGroups)
                .toList(),
            e.getEgressCidrBlocks().stream()
                .map(EgressCidrBlockGroup::getEgressCidrBlocks)
                .toList(),
            e.getEgressIpv6CidrBlocks().stream()
                .map(EgressIpv6CidrBlockGroup::getEgressIpv6CidrBlocks)
                .toList(),
            e.getEgressPrefixListIds().stream()
                .map(EgressPrefixListIdGroup::getEgressPrefixListIds)
                .toList(),
            e.getEgressSecurityGroups().stream()
                .map(EgressSecurityGroupGroup::getEgressSecurityGroups)
                .toList(),

            /** JavaBeans getter method declaration */

            e.getTags(),
            e.getResourceSaveName(),
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getVersion()
        );
    }
}
