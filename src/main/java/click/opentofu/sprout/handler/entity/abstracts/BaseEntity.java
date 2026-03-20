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
public abstract class BaseEntity {

    public BaseEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;
    private String tenantId;

    private String taskId;
    private String taskArn;
    private String accountId;
    private String region;
    private String name;

    /** Entity field declarations */

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String containerDefinitions;
    private String cpu;
    private Boolean enableFaultInjection;
    private String sizeInGib;
    private String executionRoleArn;
    private String family;
    private String ipcMode;
    private String memory;
    private String networkMode;
    private String pidMode;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> expression;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> constraintsType;
    private String containerName;
    private String proxyType;
    private String appPorts;
    private String egressIgnoredIps;
    private String ignoredUid;
    private String proxyEgressPort;
    private String proxyIngressPort;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> requiresCompatibilities;
    private String operatingSystemFamily;
    private String cpuArchitecture;
    private Boolean skipDestroy;
    private String taskRoleArn;
    private Boolean trackLatest;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> volumeName;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> hostPath;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> configureAtLaunch;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> autoprovision;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> driver;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> scope;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> efsFileSystemId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> efsRootDirectory;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> transitEncryption;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> transitEncryptionPort;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> accessPointId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> iam;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> fsxFileSystemId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> fsxRootDirectory;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> credentialsParameter;    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> domain;
}
