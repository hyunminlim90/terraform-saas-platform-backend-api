package click.opentofu.sprout.dto.response;

import java.time.LocalDateTime;

import click.opentofu.sprout.dto.interfaces.ModuleDto;
import click.opentofu.sprout.handler.entity.entity.route_table_association.entity.RouteTableAssociationEntity;

public record RouteTableAssociationDto (

    /** Parameter section for specifying the fields defined in BaseEntity.class and TofuEntity.class */

    String tenantId,
    String routeTableAssociationId,
    // String routeTableAssociationArn,
    String accountId,
    String region,
    String name,
    String moduleName,

    /** Record component definitions */

    String gatewayId,
    String routeTableId,
    String subnetId,

    /** Record component definitions */

    String resourceSaveName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long version

) implements ModuleDto {

    public static RouteTableAssociationDto from(RouteTableAssociationEntity e) {
        return new RouteTableAssociationDto(

            /** Maps a TofuEntity to a TofuDto */

            // e.get
            // e.get
            // e.get

            e.getTenantId(),
            e.getRouteTableAssociationId(),
            // e.getRouteTableAssociationArn(),
            e.getAccountId(),
            e.getRegion(),
            e.getName(),
            e.getModuleName(),

            /** JavaBeans getter method declaration */

            e.getGatewayId(),
            e.getRouteTableId(),
            e.getSubnetId(),

            /** JavaBeans getter method declaration */

            e.getResourceSaveName(),
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getVersion()
        );
    }
}
