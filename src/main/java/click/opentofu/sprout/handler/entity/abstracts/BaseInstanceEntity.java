package click.opentofu.sprout.handler.entity.abstracts;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@MappedSuperclass
public abstract class BaseInstanceEntity {

    public BaseInstanceEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;
    private String tenantId;

    private String instanceId;
    // private String instanceArn;
    private String accountId;
    private String region;
    private String name;
    private String moduleName;

    /** Entity field declarations */

    private String ami;
    private String instanceType;
    private String availabilityZone;
    private String subnetId;
    private String privateIp;
    private String keyName;
    private String iamInstanceProfile;
    private String tenancy;
    private String hostId;
    private String placementGroup;
    private String placementGroupId;
    private String hostResourceGroupArn;
    private String ipv6AddressCount;
    private String placementPartitionNumber;
    private String marketType;
    private String instanceInterruptionBehavior;
    private String maxPrice;
    private String spotInstanceType;
    private String validUntil;
    private String ltId;
    private String ltName;
    private String ltVersion;
    private String networkInterfaceId;
    private String capacityReservationPreference;
    private String capacityReservationId;
    private String capacityReservationResourceGroupArn;
    private String amdSevSnp;
    private String nestedVirtualization;
    private String cpuCredits;
    private String autoRecovery;
    private String httpEndpoint;
    private String httpProtocolIpv6;
    private String httpTokens;
    private String instanceMetadataTags;
    private String hostnameType;
    private String rootDevKmsKeyId;
    private String rootDevVolumeType;
    private String coreCount;
    private String threadsPerCore;
    private String httpPutResponseHopLimit;
    private String rootDevIops;
    private String rootDevThroughput;
    private String rootDevVolumeSize;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String userData;

    private Boolean associatePublicIpAddress;
    private Boolean disableApiStop;
    private Boolean disableApiTermination;
    private Boolean ebsOptimized;
    private Boolean forceDestroy;
    private Boolean getPasswordData;
    private Boolean hibernation;
    private Boolean monitoring;
    private Boolean sourceDestCheck;
    private Boolean userDataReplaceOnChange;
    private Boolean enablePrimaryIpv6;
    private Boolean priEniDeleteOnTermination;
    private Boolean enabled;
    private Boolean enableResourceNameDnsAaaaRecord;
    private Boolean enableResourceNameDnsARecord;
    private Boolean rootDevDeleteOnTermination;
    private Boolean rootDevEncrypted;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ipv6Addresses;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> secondaryPrivateIps;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> vpcSecurityGroupIds;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> securityGroups;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ebsDevDeviceName;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ebsDevKmsKeyId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ebsDevSnapshotId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ebsDevVolumeType;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ephDevDeviceName;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> virtualName;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> secondarySubnetId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> interfaceType;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ebsDevDeleteOnTermination;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ebsDevEncrypted;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> noDevice;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> secEniDeleteOnTermination;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ebsDevIops;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ebsDevThroughput;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> ebsDevVolumeSize;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> networkCardIndex;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> deviceIndex;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> privateIpAddressCount;
}
