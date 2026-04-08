package click.opentofu.sprout.handler.entity.entity.security_group.relation;

import java.util.ArrayList;
import java.util.List;

import click.opentofu.sprout.handler.entity.entity.security_group.entity.SecurityGroupEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Data
@Entity
@Table(name = "security_group_entity_ingress_security_group_group")
public class IngressSecurityGroupGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "security_group_entity_pk")
    private SecurityGroupEntity securityGroupEntity;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "security_group_entity_ingress_security_groups", joinColumns = @JoinColumn(name = "ingress_security_group_group_id"))
    @Column(name = "ingress_security_group_value")
    private List<String> ingressSecurityGroups = new ArrayList<>();
}
