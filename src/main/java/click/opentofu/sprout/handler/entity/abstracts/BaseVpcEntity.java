package click.opentofu.sprout.handler.entity.abstracts;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@MappedSuperclass
public abstract class BaseVpcEntity {

    public BaseVpcEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;
    private String tenantId;

    private String vpcId;
    private String accountId;
    private String region;
    private String name;
    private String moduleName;

    /** Entity field declarations */

    private Boolean assignGeneratedIpv6CidrBlock;
    private String cidrBlock;
    private Boolean enableDnsHostnames;
    private Boolean enableDnsSupport;
    private Boolean enableNetworkAddressUsageMetrics;
    private String instanceTenancy;
    private String ipv4IpamPoolId;
    private String ipv4NetmaskLength;
    private String ipv6CidrBlock;
    private String ipv6CidrBlockNetworkBorderGroup;
    private String ipv6IpamPoolId;
    private String ipv6NetmaskLength;
}
