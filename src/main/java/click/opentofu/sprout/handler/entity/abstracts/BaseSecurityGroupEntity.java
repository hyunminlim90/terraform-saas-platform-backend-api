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
public abstract class BaseSecurityGroupEntity {

    public BaseSecurityGroupEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;
    private String tenantId;

    private String securityGroupId;
    // private String securityGroupArn;
    private String accountId;
    private String region;
    private String name;
    private String moduleName;

    /** Entity field declarations */

    private String description;
    private String securityGroupName;
    private String namePrefix;
    private String vpcId;
    private Boolean revokeRulesOnDelete;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ingressProtocol;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ingressDescription;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> egressProtocol;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> egressDescription;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ingressFromPort;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ingressToPort;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> egressFromPort;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> egressToPort;    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ingressSelf;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> egressSelf;
}
