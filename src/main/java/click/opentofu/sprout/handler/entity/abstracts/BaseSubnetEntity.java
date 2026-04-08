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
public abstract class BaseSubnetEntity {

    public BaseSubnetEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;
    private String tenantId;

    private String subnetId;
    // private String subnetArn;
    private String accountId;
    private String region;
    private String name;
    private String moduleName;

    /** Entity field declarations */

    private String vpcId;
    private Boolean assignIpv6AddressOnCreation;
    private String availabilityZone;
    private String availabilityZoneId;
    private String cidrBlock;
    private String customerOwnedIpv4Pool;
    private Boolean enableDns64;
    private String enableLniAtDeviceIndex;
    private Boolean enableResourceNameDnsARecordOnLaunch;
    private Boolean enableResourceNameDnsAaaaRecordOnLaunch;
    private String ipv6CidrBlock;
    private Boolean ipv6Native;
    private Boolean mapCustomerOwnedIpOnLaunch;
    private Boolean mapPublicIpOnLaunch;
    private String outpostArn;
    private String privateDnsHostnameTypeOnLaunch;
}
