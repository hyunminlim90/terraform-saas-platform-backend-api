package click.opentofu.sprout.handler.entity.entity.instance.entity;

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

import click.opentofu.sprout.handler.entity.abstracts.BaseInstanceEntity;
import click.opentofu.sprout.handler.entity.entity.instance.relation.EbsDevTagGroup;
import click.opentofu.sprout.handler.entity.entity.instance.relation.PrivateIpAddressGroup;
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
@Entity(name = "instance_entity")
@Table(name = "instance_entity")
public class InstanceEntity extends BaseInstanceEntity {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "instance_entity_tags", joinColumns = @JoinColumn(name = "instance_entity_pk"))
    @MapKeyColumn(name = "tag_key")
    @Column(name = "tag_value")
    @Builder.Default
    private Map<String, String> tags = new HashMap<>();

    /** Collection Mapping Accessors (Begin) */

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "instance_entity_root_dev_tags", joinColumns = @JoinColumn(name = "instance_entity_pk"))
    @MapKeyColumn(name = "root_dev_tag_key")
    @Column(name = "root_dev_tag_value")
    @Builder.Default
    private Map<String, String> rootDevTags = new HashMap<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "instance_entity_volume_tags", joinColumns = @JoinColumn(name = "instance_entity_pk"))
    @MapKeyColumn(name = "volume_tag_key")
    @Column(name = "volume_tag_value")
    @Builder.Default
    private Map<String, String> volumeTags = new HashMap<>();
    
    @OneToMany(mappedBy = "instanceEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size = 50)
    private List<EbsDevTagGroup> ebsDevTags = new ArrayList<>();

    @JsonGetter("ebsDevTags")
    public List<Map<String, String>> getEbsDevTagsAsMap() {
        return this.ebsDevTags.stream()
            .map((ebsDevTagGroup) -> ebsDevTagGroup.getEbsDevTags())
            .collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "instanceEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size = 50)
    private List<PrivateIpAddressGroup> privateIpAddresses = new ArrayList<>();

    @JsonGetter("privateIpAddresses")
    public List<List<String>> getPrivateIpAddressesAsList() {
        return this.privateIpAddresses.stream()
            .map((privateIpAddressGroup) -> privateIpAddressGroup.getPrivateIpAddresses())
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
