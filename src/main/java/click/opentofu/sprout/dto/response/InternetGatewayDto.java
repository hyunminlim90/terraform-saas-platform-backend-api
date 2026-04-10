package click.opentofu.sprout.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

import click.opentofu.sprout.dto.interfaces.ModuleDto;
import click.opentofu.sprout.handler.entity.entity.internet_gateway.entity.InternetGatewayEntity;

public record InternetGatewayDto (

    /** Parameter section for specifying the fields defined in BaseEntity.class and TofuEntity.class */

    String tenantId,
    String internetGatewayId,
    // String internetGatewayArn,
    String accountId,
    String region,
    String name,
    String moduleName,

    /** Record component definitions */

    String vpcId,

    /** Record component definitions */

    Map<String, String> tags,
    String resourceSaveName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long version

) implements ModuleDto {

    public static InternetGatewayDto from(InternetGatewayEntity e) {
        return new InternetGatewayDto(

            /** Maps a TofuEntity to a TofuDto */

            // e.get
            // e.get
            // e.get

            e.getTenantId(),
            e.getInternetGatewayId(),
            // e.getInternetGatewayArn(),
            e.getAccountId(),
            e.getRegion(),
            e.getName(),
            e.getModuleName(),

            /** JavaBeans getter method declaration */

            e.getVpcId(),

            /** JavaBeans getter method declaration */

            e.getTags(),
            e.getResourceSaveName(),
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getVersion()
        );
    }
}
