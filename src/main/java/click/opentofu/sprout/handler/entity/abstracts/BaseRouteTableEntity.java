package click.opentofu.sprout.handler.entity.abstracts;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@MappedSuperclass
public abstract class BaseRouteTableEntity {

    public BaseRouteTableEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;
    private String tenantId;

    private String routeTableId;
    // private String routeTableArn;
    private String accountId;
    private String region;
    private String name;
    private String moduleName;

    /** Entity field declarations */

    private String vpcId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> propagatingVgws;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> cidrBlock;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ipv6CidrBlock;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> destinationPrefixListId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> carrierGatewayId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> coreNetworkArn;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> egressOnlyGatewayId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> gatewayId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> localGatewayId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> natGatewayId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> networkInterfaceId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> transitGatewayId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> vpcEndpointId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> vpcPeeringConnectionId;
}
