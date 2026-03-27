package click.opentofu.sprout.handler.entity.abstracts;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@MappedSuperclass
public abstract class BaseVpcIpamPoolEntity {

    public BaseVpcIpamPoolEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;
    private String tenantId;

    private String vpcIpamPoolId;
    private String vpcIpamPoolArn;
    private String accountId;
    private String region;
    private String name;
    private String moduleName;

    /** Entity field declarations */

    private String addressFamily;
    private String awsService;
    private String description;
    private String ipamScopeId;
    private String locale;
    private String publicIpSource;
    private String sourceIpamPoolId;
    private String allocationDefaultNetmaskLength;
    private String allocationMaxNetmaskLength;
    private String allocationMinNetmaskLength;
    private Boolean autoImport;
    @Column(name = "cascade_flag")
    private Boolean cascade;
    private Boolean publiclyAdvertisable;
}
