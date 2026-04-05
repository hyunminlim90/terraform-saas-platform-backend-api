package click.opentofu.sprout.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import click.opentofu.sprout.dto.interfaces.ModuleDto;
import click.opentofu.sprout.handler.entity.entity.instance.entity.InstanceEntity;
import click.opentofu.sprout.handler.entity.entity.instance.relation.EbsDevTagGroup;
import click.opentofu.sprout.handler.entity.entity.instance.relation.PrivateIpAddressGroup;

public record InstanceDto (

    /** Parameter section for specifying the fields defined in BaseEntity.class and TofuEntity.class */

    String tenantId,
    String instanceId,
    // String instanceArn,
    String accountId,
    String region,
    String name,
    String moduleName,

    /** Record component definitions */

    String ami,
    String instanceType,
    String availabilityZone,
    String subnetId,
    String privateIp,
    String keyName,
    String iamInstanceProfile,
    String tenancy,
    String hostId,
    String placementGroup,
    String placementGroupId,
    String hostResourceGroupArn,
    String ipv6AddressCount,
    String placementPartitionNumber,
    String userData,
    String marketType,
    String instanceInterruptionBehavior,
    String maxPrice,
    String spotInstanceType,
    String validUntil,
    String ltId,
    String ltName,
    String ltVersion,
    String networkInterfaceId,
    String capacityReservationPreference,
    String capacityReservationId,
    String capacityReservationResourceGroupArn,
    String amdSevSnp,
    String nestedVirtualization,
    String cpuCredits,
    String autoRecovery,
    String httpEndpoint,
    String httpProtocolIpv6,
    String httpTokens,
    String instanceMetadataTags,
    String hostnameType,
    String rootDevKmsKeyId,
    String rootDevVolumeType,
    String coreCount,
    String threadsPerCore,
    String httpPutResponseHopLimit,
    String rootDevIops,
    String rootDevThroughput,
    String rootDevVolumeSize,
    Boolean associatePublicIpAddress,
    Boolean disableApiStop,
    Boolean disableApiTermination,
    Boolean ebsOptimized,
    Boolean forceDestroy,
    Boolean getPasswordData,
    Boolean hibernation,
    Boolean monitoring,
    Boolean sourceDestCheck,
    Boolean userDataReplaceOnChange,
    Boolean enablePrimaryIpv6,
    Boolean priEniDeleteOnTermination,
    Boolean enabled,
    Boolean enableResourceNameDnsAaaaRecord,
    Boolean enableResourceNameDnsARecord,
    Boolean rootDevDeleteOnTermination,
    Boolean rootDevEncrypted,
    List<String> ipv6Addresses,
    List<String> secondaryPrivateIps,
    List<String> vpcSecurityGroupIds,
    List<String> securityGroups,
    List<String> ebsDevDeviceName,
    List<String> ebsDevKmsKeyId,
    List<String> ebsDevSnapshotId,
    List<String> ebsDevVolumeType,
    List<String> ephDevDeviceName,
    List<String> virtualName,
    List<String> secondarySubnetId,
    List<String> interfaceType,
    List<String> ebsDevDeleteOnTermination,
    List<String> ebsDevEncrypted,
    List<String> noDevice,
    List<String> secEniDeleteOnTermination,
    List<String> ebsDevIops,
    List<String> ebsDevThroughput,
    List<String> ebsDevVolumeSize,
    List<String> networkCardIndex,
    List<String> deviceIndex,
    List<String> privateIpAddressCount,
    Map<String, String> rootDevTags,
    Map<String, String> volumeTags,
    List<Map<String, String>> ebsDevTags,
    List<List<String>> privateIpAddresses,

    /** Record component definitions */

    Map<String, String> tags,
    String resourceSaveName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long version

) implements ModuleDto {

    public static InstanceDto from(InstanceEntity e) {
        return new InstanceDto(

            /** Maps a TofuEntity to a TofuDto */

            // e.get
            // e.get
            // e.get

            e.getTenantId(),
            e.getInstanceId(),
            // e.getInstanceArn(),
            e.getAccountId(),
            e.getRegion(),
            e.getName(),
            e.getModuleName(),

            /** JavaBeans getter method declaration */

            e.getAmi(),
            e.getInstanceType(),
            e.getAvailabilityZone(),
            e.getSubnetId(),
            e.getPrivateIp(),
            e.getKeyName(),
            e.getIamInstanceProfile(),
            e.getTenancy(),
            e.getHostId(),
            e.getPlacementGroup(),
            e.getPlacementGroupId(),
            e.getHostResourceGroupArn(),
            e.getIpv6AddressCount(),
            e.getPlacementPartitionNumber(),

            e.getUserData(),
            e.getMarketType(),
            e.getInstanceInterruptionBehavior(),
            e.getMaxPrice(),
            e.getSpotInstanceType(),
            e.getValidUntil(),
            e.getLtId(),
            e.getLtName(),
            e.getLtVersion(),
            e.getNetworkInterfaceId(),
            e.getCapacityReservationPreference(),
            e.getCapacityReservationId(),
            e.getCapacityReservationResourceGroupArn(),
            e.getAmdSevSnp(),
            e.getNestedVirtualization(),
            e.getCpuCredits(),
            e.getAutoRecovery(),
            e.getHttpEndpoint(),
            e.getHttpProtocolIpv6(),
            e.getHttpTokens(),
            e.getInstanceMetadataTags(),
            e.getHostnameType(),
            e.getRootDevKmsKeyId(),
            e.getRootDevVolumeType(),
            e.getCoreCount(),
            e.getThreadsPerCore(),
            e.getHttpPutResponseHopLimit(),
            e.getRootDevIops(),
            e.getRootDevThroughput(),
            e.getRootDevVolumeSize(),

            e.getAssociatePublicIpAddress(),
            e.getDisableApiStop(),
            e.getDisableApiTermination(),
            e.getEbsOptimized(),
            e.getForceDestroy(),
            e.getGetPasswordData(),
            e.getHibernation(),
            e.getMonitoring(),
            e.getSourceDestCheck(),
            e.getUserDataReplaceOnChange(),
            e.getEnablePrimaryIpv6(),
            e.getPriEniDeleteOnTermination(),
            e.getEnabled(),
            e.getEnableResourceNameDnsAaaaRecord(),
            e.getEnableResourceNameDnsARecord(),
            e.getRootDevDeleteOnTermination(),
            e.getRootDevEncrypted(),

            e.getIpv6Addresses(),
            e.getSecondaryPrivateIps(),
            e.getVpcSecurityGroupIds(),
            e.getSecurityGroups(),
            e.getEbsDevDeviceName(),
            e.getEbsDevKmsKeyId(),
            e.getEbsDevSnapshotId(),
            e.getEbsDevVolumeType(),
            e.getEphDevDeviceName(),
            e.getVirtualName(),
            e.getSecondarySubnetId(),
            e.getInterfaceType(),
            e.getEbsDevDeleteOnTermination(),
            e.getEbsDevEncrypted(),
            e.getNoDevice(),
            e.getSecEniDeleteOnTermination(),
            e.getEbsDevIops(),
            e.getEbsDevThroughput(),
            e.getEbsDevVolumeSize(),
            e.getNetworkCardIndex(),
            e.getDeviceIndex(),
            e.getPrivateIpAddressCount(),

            e.getRootDevTags(),
            e.getVolumeTags(),

            e.getEbsDevTags().stream()
                .map(EbsDevTagGroup::getEbsDevTags)
                .toList(),

            e.getPrivateIpAddresses().stream()
                .map(PrivateIpAddressGroup::getPrivateIpAddresses)
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
