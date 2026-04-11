package click.opentofu.sprout.handler.entity.abstracts;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@MappedSuperclass
public abstract class BaseRouteTableAssociationEntity {

    public BaseRouteTableAssociationEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;
    private String tenantId;

    private String routeTableAssociationId;
    // private String routeTableAssociationArn;
    private String accountId;
    private String region;
    private String name;
    private String moduleName;

    /** Entity field declarations */

    private String gatewayId;
    private String routeTableId;
    private String subnetId;
}
