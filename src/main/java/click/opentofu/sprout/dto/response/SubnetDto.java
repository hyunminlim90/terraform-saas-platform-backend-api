package click.opentofu.sprout.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

import click.opentofu.sprout.dto.interfaces.ModuleDto;
import click.opentofu.sprout.handler.entity.entity.subnet.entity.SubnetEntity;

public record SubnetDto (

    /** Parameter section for specifying the fields defined in BaseEntity.class and TofuEntity.class */

    String tenantId,
    String subnetId,
    // String subnetArn,
    String accountId,
    String region,
    String name,
    String moduleName,

    /** Record component definitions */

    String vpcId,
    Boolean assignIpv6AddressOnCreation,
    String availabilityZone,
    String availabilityZoneId,
    String cidrBlock,
    String customerOwnedIpv4Pool,
    Boolean enableDns64,
    String enableLniAtDeviceIndex,
    Boolean enableResourceNameDnsARecordOnLaunch,
    Boolean enableResourceNameDnsAaaaRecordOnLaunch,
    String ipv6CidrBlock,
    Boolean ipv6Native,
    Boolean mapCustomerOwnedIpOnLaunch,
    Boolean mapPublicIpOnLaunch,
    String outpostArn,
    String privateDnsHostnameTypeOnLaunch,

    /** Record component definitions */

    Map<String, String> tags,
    String resourceSaveName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long version

) implements ModuleDto {

    public static SubnetDto from(SubnetEntity e) {
        return new SubnetDto(

            /** Maps a TofuEntity to a TofuDto */

            // e.get
            // e.get
            // e.get

            e.getTenantId(),
            e.getSubnetId(),
            // e.getSubnetArn(),
            e.getAccountId(),
            e.getRegion(),
            e.getName(),
            e.getModuleName(),

            /** JavaBeans getter method declaration */

            e.getVpcId(),
            e.getAssignIpv6AddressOnCreation(),
            e.getAvailabilityZone(),
            e.getAvailabilityZoneId(),
            e.getCidrBlock(),
            e.getCustomerOwnedIpv4Pool(),
            e.getEnableDns64(),
            e.getEnableLniAtDeviceIndex(),
            e.getEnableResourceNameDnsARecordOnLaunch(),
            e.getEnableResourceNameDnsAaaaRecordOnLaunch(),
            e.getIpv6CidrBlock(),
            e.getIpv6Native(),
            e.getMapCustomerOwnedIpOnLaunch(),
            e.getMapPublicIpOnLaunch(),
            e.getOutpostArn(),
            e.getPrivateDnsHostnameTypeOnLaunch(),

            /** JavaBeans getter method declaration */

            e.getTags(),
            e.getResourceSaveName(),
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getVersion()
        );
    }
}
