package click.opentofu.sprout.handler.entity.entity.vpc.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import click.opentofu.sprout.handler.entity.abstracts.BaseVpcEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
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
@Entity(name = "vpc_entity")
@Table(name = "vpc_entity")
public class VpcEntity extends BaseVpcEntity {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vpc_entity_tags", joinColumns = @JoinColumn(name = "vpc_entity_pk"))
    @MapKeyColumn(name = "tag_key")
    @Column(name = "tag_value")
    @Builder.Default
    private Map<String, String> tags = new HashMap<>();

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
