package click.opentofu.sprout.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

import click.opentofu.sprout.dto.interfaces.ModuleDto;
import click.opentofu.sprout.handler.entity.entity.vpc.entity.VpcEntity;

public record VpcDto (

    /** Parameter section for specifying the fields defined in BaseEntity.class and TofuEntity.class */

    String tenantId,
    String vpcId,
    // String vpcArn,
    String accountId,
    String region,
    String name,
    String moduleName,

    /** Record component definitions */

    Boolean assignGeneratedIpv6CidrBlock,
    String cidrBlock,
    Boolean enableDnsHostnames,
    Boolean enableDnsSupport,
    Boolean enableNetworkAddressUsageMetrics,
    String instanceTenancy,
    String ipv4IpamPoolId,
    String ipv4NetmaskLength,
    String ipv6CidrBlock,
    String ipv6CidrBlockNetworkBorderGroup,
    String ipv6IpamPoolId,
    String ipv6NetmaskLength,

    /** Record component definitions */

    Map<String, String> tags,
    String resourceSaveName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long version

) implements ModuleDto {

    public static VpcDto from(VpcEntity e) {
        return new VpcDto(

            /** Maps a TofuEntity to a TofuDto */

            // e.get
            // e.get
            // e.get

            e.getTenantId(),
            e.getVpcId(),
            // e.getVpcArn(),
            e.getAccountId(),
            e.getRegion(),
            e.getName(),
            e.getModuleName(),

            /** JavaBeans getter method declaration */

            e.getAssignGeneratedIpv6CidrBlock(),
            e.getCidrBlock(),
            e.getEnableDnsHostnames(),
            e.getEnableDnsSupport(),
            e.getEnableNetworkAddressUsageMetrics(),
            e.getInstanceTenancy(),
            e.getIpv4IpamPoolId(),
            e.getIpv4NetmaskLength(),
            e.getIpv6CidrBlock(),
            e.getIpv6CidrBlockNetworkBorderGroup(),
            e.getIpv6IpamPoolId(),
            e.getIpv6NetmaskLength(),

            /** JavaBeans getter method declaration */

            e.getTags(),
            e.getResourceSaveName(),
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getVersion()
        );
    }
}
