package click.opentofu.sprout.handler.entity.entity.sts.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "sts_entity")
@Table(name = "sts_entity")
public class StsEntity {

    @Id
    private String accountId;
    private String alias;
    private String roleName;
    private String authEmailId;
    private String awsAccessKey;
    private String awsSecretAccessKey;
    @Column(length = 2048)
    private String awsSessionToken;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
