package click.opentofu.sprout.handler.entity.entity.instance.relation;

import java.util.HashMap;
import java.util.Map;

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
import jakarta.persistence.MapKeyColumn;

@Data
@Entity
@Table(name = "instance_entity_ebs_dev_tag_group")
public class EbsDevTagGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_entity_pk")
    private InstanceEntity instanceEntity;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "instance_entity_ebs_dev_tags", joinColumns = @JoinColumn(name = "ebs_dev_tag_group_id"))
    @MapKeyColumn(name = "ebs_dev_tag_key")
    @Column(name = "ebs_dev_tag_value")
    private Map<String, String> ebsDevTags = new HashMap<>();
}
