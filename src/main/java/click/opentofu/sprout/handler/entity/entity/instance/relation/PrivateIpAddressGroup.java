package click.opentofu.sprout.handler.entity.entity.instance.relation;

import java.util.ArrayList;
import java.util.List;

import click.opentofu.sprout.handler.entity.entity.instance.entity.InstanceEntity;
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
@Table(name = "instance_entity_private_ip_address_group")
public class PrivateIpAddressGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_entity_pk")
    private InstanceEntity instanceEntity;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "instance_entity_private_ip_addresses", joinColumns = @JoinColumn(name = "private_ip_address_group_id"))
    @Column(name = "private_ip_address_value")
    private List<String> privateIpAddresses = new ArrayList<>();
}
