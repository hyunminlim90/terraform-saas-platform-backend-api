package click.opentofu.sprout.handler.entity.entity;

import java.util.HashMap;
import java.util.Map;

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
@Table(name = "sprout_entity_label_group")
public class LabelGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprout_entity_pk")
    private TofuEntity tofuEntity;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sprout_entity_labels", joinColumns = @JoinColumn(name = "label_group_id"))
    @MapKeyColumn(name = "label_key")
    @Column(name = "label_value")
    private Map<String, String> labels = new HashMap<>();
}
