package click.opentofu.sprout.handler.entity.entity.vpc_ipam_pool.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import click.opentofu.sprout.handler.entity.abstracts.BaseVpcIpamPoolEntity;
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
@Entity(name = "vpc_ipam_pool_entity")
@Table(name = "vpc_ipam_pool_entity")
public class VpcIpamPoolEntity extends BaseVpcIpamPoolEntity {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vpc_ipam_pool_entity_tags", joinColumns = @JoinColumn(name = "vpc_ipam_pool_entity_pk"))
    @MapKeyColumn(name = "tag_key")
    @Column(name = "tag_value")
    @Builder.Default
    private Map<String, String> tags = new HashMap<>();

    /** Collection Mapping Accessors (Begin) */

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vpc_ipam_pool_entity_allocation_resource_tags", joinColumns = @JoinColumn(name = "vpc_ipam_pool_entity_pk"))
    @MapKeyColumn(name = "allocation_resource_tag_key")
    @Column(name = "allocation_resource_tag_value")
    @Builder.Default
    private Map<String, String> allocationResourceTags = new HashMap<>();

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
