package click.opentofu.sprout.handler.entity.entity;

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

import click.opentofu.sprout.handler.entity.abstracts.BaseEntity;
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
@Entity(name = "sprout_entity")
@Table(name = "sprout_entity")
public class TofuEntity extends BaseEntity {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sprout_entity_tags", joinColumns = @JoinColumn(name = "sprout_entity_pk"))
    @MapKeyColumn(name = "tag_key")
    @Column(name = "tag_value")
    @Builder.Default
    private Map<String, String> tags = new HashMap<>();

    /** Collection Mapping Accessors (Begin) */

    @OneToMany(mappedBy = "tofuEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size = 50)
    private List<LabelGroup> labels = new ArrayList<>();

    @JsonGetter("labels")
    public List<Map<String, String>> getLabelsAsMap() {
        return this.labels.stream()
            .map((labelGroup) -> labelGroup.getLabels())
            .collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "tofuEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size = 50)
    private List<DriverOptGroup> driverOpts = new ArrayList<>();

    @JsonGetter("driverOpts")
    public List<Map<String, String>> getDriverOptsAsMap() {
        return this.driverOpts.stream()
            .map((driverOptGroup) -> driverOptGroup.getOpts())
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
