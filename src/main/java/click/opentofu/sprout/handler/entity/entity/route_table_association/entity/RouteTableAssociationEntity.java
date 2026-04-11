package click.opentofu.sprout.handler.entity.entity.route_table_association.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import click.opentofu.sprout.handler.entity.abstracts.BaseRouteTableAssociationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity(name = "route_table_association_entity")
@Table(name = "route_table_association_entity")
public class RouteTableAssociationEntity extends BaseRouteTableAssociationEntity {

    /** Collection Mapping Accessors (Begin) */

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
