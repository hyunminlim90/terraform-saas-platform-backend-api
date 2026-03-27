package click.opentofu.sprout.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

import click.opentofu.sprout.dto.interfaces.ModuleDto;
import click.opentofu.sprout.handler.entity.entity.vpc_ipam_pool.entity.VpcIpamPoolEntity;

public record VpcIpamPoolDto (

    /** Parameter section for specifying the fields defined in BaseEntity.class and TofuEntity.class */

    String tenantId,
    String vpcIpamPoolId,
    String vpcIpamPoolArn,
    String accountId,
    String region,
    String name,
    String moduleName,

    /** Record component definitions */

    String addressFamily,
    String awsService,
    String description,
    String ipamScopeId,
    String locale,
    String publicIpSource,
    String sourceIpamPoolId,
    String allocationDefaultNetmaskLength,
    String allocationMaxNetmaskLength,
    String allocationMinNetmaskLength,
    Boolean autoImport,
    Boolean cascade,
    Boolean publiclyAdvertisable,
    Map<String, String> allocationResourceTags,

    /** Record component definitions */

    Map<String, String> tags,
    String resourceSaveName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long version

) implements ModuleDto {

    public static VpcIpamPoolDto from(VpcIpamPoolEntity e) {
        return new VpcIpamPoolDto(

            /** Maps a TofuEntity to a TofuDto */

            // e.get
            // e.get
            // e.get

            e.getTenantId(),
            e.getVpcIpamPoolId(),
            e.getVpcIpamPoolArn(),
            e.getAccountId(),
            e.getRegion(),
            e.getName(),
            e.getModuleName(),

            /** JavaBeans getter method declaration */

            e.getAddressFamily(),
            e.getAwsService(),
            e.getDescription(),
            e.getIpamScopeId(),
            e.getLocale(),
            e.getPublicIpSource(),
            e.getSourceIpamPoolId(),
            e.getAllocationDefaultNetmaskLength(),
            e.getAllocationMaxNetmaskLength(),
            e.getAllocationMinNetmaskLength(),
            e.getAutoImport(),
            e.getCascade(),
            e.getPubliclyAdvertisable(),
            e.getAllocationResourceTags(),

            /** JavaBeans getter method declaration */

            e.getTags(),
            e.getResourceSaveName(),
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getVersion()
        );
    }
}
