package click.opentofu.sprout.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import click.opentofu.sprout.dto.interfaces.ModuleDto;
import click.opentofu.sprout.handler.entity.entity.route_table.entity.RouteTableEntity;

public record RouteTableDto (

    /** Parameter section for specifying the fields defined in BaseEntity.class and TofuEntity.class */

    String tenantId,
    String routeTableId,
    // String routeTableArn,
    String accountId,
    String region,
    String name,
    String moduleName,

    /** Record component definitions */

    String vpcId,
    List<String> propagatingVgws,
    List<String> cidrBlock,
    List<String> ipv6CidrBlock,
    List<String> destinationPrefixListId,
    List<String> carrierGatewayId,
    List<String> coreNetworkArn,
    List<String> egressOnlyGatewayId,
    List<String> gatewayId,
    List<String> localGatewayId,
    List<String> natGatewayId,
    List<String> networkInterfaceId,
    List<String> transitGatewayId,
    List<String> vpcEndpointId,
    List<String> vpcPeeringConnectionId,

    /** Record component definitions */

    Map<String, String> tags,
    String resourceSaveName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long version

) implements ModuleDto {

    public static RouteTableDto from(RouteTableEntity e) {
        return new RouteTableDto(

            /** Maps a TofuEntity to a TofuDto */

            // e.get
            // e.get
            // e.get

            e.getTenantId(),
            e.getRouteTableId(),
            // e.getRouteTableArn(),
            e.getAccountId(),
            e.getRegion(),
            e.getName(),
            e.getModuleName(),

            /** JavaBeans getter method declaration */

            e.getVpcId(),
            e.getPropagatingVgws(),
            e.getCidrBlock(),
            e.getIpv6CidrBlock(),
            e.getDestinationPrefixListId(),
            e.getCarrierGatewayId(),
            e.getCoreNetworkArn(),
            e.getEgressOnlyGatewayId(),
            e.getGatewayId(),
            e.getLocalGatewayId(),
            e.getNatGatewayId(),
            e.getNetworkInterfaceId(),
            e.getTransitGatewayId(),
            e.getVpcEndpointId(),
            e.getVpcPeeringConnectionId(),

            /** JavaBeans getter method declaration */

            e.getTags(),
            e.getResourceSaveName(),
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getVersion()
        );
    }
}
