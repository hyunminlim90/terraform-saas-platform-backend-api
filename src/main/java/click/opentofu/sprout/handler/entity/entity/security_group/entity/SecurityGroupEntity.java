package click.opentofu.sprout.handler.entity.entity.security_group.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonGetter;

import click.opentofu.sprout.handler.entity.abstracts.BaseSecurityGroupEntity;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.EgressCidrBlockGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.EgressIpv6CidrBlockGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.EgressPrefixListIdGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.EgressSecurityGroupGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.IngressCidrBlockGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.IngressIpv6CidrBlockGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.IngressPrefixListIdGroup;
import click.opentofu.sprout.handler.entity.entity.security_group.relation.IngressSecurityGroupGroup;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity(name = "security_group_entity")
@Table(name = "security_group_entity")
public class SecurityGroupEntity extends BaseSecurityGroupEntity {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "security_group_entity_tags", joinColumns = @JoinColumn(name = "security_group_entity_pk"))
    @MapKeyColumn(name = "tag_key")
    @Column(name = "tag_value")
    @Builder.Default
    private Map<String, String> tags = new HashMap<>();

    /** Collection Mapping Accessors (Begin) */

    @OneToMany(mappedBy = "securityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size = 50)
    private List<IngressCidrBlockGroup> ingressCidrBlocks = new ArrayList<>();

    @JsonGetter("ingressCidrBlocks")
    public List<List<String>> getIngressCidrBlocksAsList() {
        return this.ingressCidrBlocks.stream()
            .map((ingressCidrBlockGroup) -> ingressCidrBlockGroup.getIngressCidrBlocks())
            .collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "securityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size = 50)
    private List<IngressIpv6CidrBlockGroup> ingressIpv6CidrBlocks = new ArrayList<>();

    @JsonGetter("ingressIpv6CidrBlocks")
    public List<List<String>> getIngressIpv6CidrBlocksAsList() {
        return this.ingressIpv6CidrBlocks.stream()
            .map((ingressIpv6CidrBlockGroup) -> ingressIpv6CidrBlockGroup.getIngressIpv6CidrBlocks())
            .collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "securityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size = 50)
    private List<IngressPrefixListIdGroup> ingressPrefixListIds = new ArrayList<>();

    @JsonGetter("ingressPrefixListIds")
    public List<List<String>> getIngressPrefixListIdsAsList() {
        return this.ingressPrefixListIds.stream()
            .map((ingressPrefixListIdGroup) -> ingressPrefixListIdGroup.getIngressPrefixListIds())
            .collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "securityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size = 50)
    private List<IngressSecurityGroupGroup> ingressSecurityGroups = new ArrayList<>();

    @JsonGetter("ingressSecurityGroups")
    public List<List<String>> getIngressSecurityGroupsAsList() {
        return this.ingressSecurityGroups.stream()
            .map((ingressSecurityGroupGroup) -> ingressSecurityGroupGroup.getIngressSecurityGroups())
            .collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "securityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size = 50)
    private List<EgressCidrBlockGroup> egressCidrBlocks = new ArrayList<>();

    @JsonGetter("egressCidrBlocks")
    public List<List<String>> getEgressCidrBlocksAsList() {
        return this.egressCidrBlocks.stream()
            .map((egressCidrBlockGroup) -> egressCidrBlockGroup.getEgressCidrBlocks())
            .collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "securityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size = 50)
    private List<EgressIpv6CidrBlockGroup> egressIpv6CidrBlocks = new ArrayList<>();

    @JsonGetter("egressIpv6CidrBlocks")
    public List<List<String>> getEgressIpv6CidrBlocksAsList() {
        return this.egressIpv6CidrBlocks.stream()
            .map((egressIpv6CidrBlockGroup) -> egressIpv6CidrBlockGroup.getEgressIpv6CidrBlocks())
            .collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "securityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size = 50)
    private List<EgressPrefixListIdGroup> egressPrefixListIds = new ArrayList<>();

    @JsonGetter("egressPrefixListIds")
    public List<List<String>> getEgressPrefixListIdsAsList() {
        return this.egressPrefixListIds.stream()
            .map((egressPrefixListIdGroup) -> egressPrefixListIdGroup.getEgressPrefixListIds())
            .collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "securityGroupEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size = 50)
    private List<EgressSecurityGroupGroup> egressSecurityGroups = new ArrayList<>();

    @JsonGetter("egressSecurityGroups")
    public List<List<String>> getEgressSecurityGroupsAsList() {
        return this.egressSecurityGroups.stream()
            .map((egressSecurityGroupGroup) -> egressSecurityGroupGroup.getEgressSecurityGroups())
            .collect(Collectors.toList());
    }

    /** Collection Mapping Accessors (End) */

    @Column(name = "resource_save_name")
    private String resourceSaveName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
