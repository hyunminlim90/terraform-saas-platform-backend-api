package click.opentofu.sprout.handler.entity.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.handler.entity.entity.DriverOptGroup;
import click.opentofu.sprout.handler.entity.entity.LabelGroup;
import click.opentofu.sprout.handler.entity.entity.TofuEntity;

public interface EntityHandler {
    default void buildEntityAndSave (JsonNode parameters, String resourceSaveName, String authUserIndex) {};
    default void duplicateBuildEntityAndSave (JsonNode parameters, String authUserIndex, String accountId, String region) {};

    default Map<String, String> parseTags (JsonNode tagsNode) {
        Map<String, String> tagsMap = new HashMap<>();
        if (!tagsNode.isMissingNode()) {
            for (Map.Entry<String, JsonNode> entry : tagsNode.properties()) {
                tagsMap.put(entry.getKey(), entry.getValue().asText());
            }
        }
        return tagsMap;
    }

    default List<String> parseToListString (JsonNode parameters) {
        List<String> result = new ArrayList<>();
        if (parameters.isArray()) {
            for (JsonNode parameter : parameters) {
                result.add(parameter.asText());
            }
        }
        return result;
    }

    /** Declaration of parse* methods for the Join entity handler */

    default List<LabelGroup> parseToLabelGroupList (JsonNode parameters, TofuEntity tofuEntity) {
        List<LabelGroup> result = new ArrayList<>();
        if (parameters.isArray()) {
            for (JsonNode parameter : parameters) {
                if (parameter.isObject()) {
                    Map<String, String> map = new HashMap<>();
                    Iterator<String> fieldNames = parameter.fieldNames();
                    while (fieldNames.hasNext()) {
                        String key = fieldNames.next();
                        String value = parameter.get(key).asText();
                        map.put(key, value);
                    }
                    LabelGroup group = new LabelGroup();
                    group.setLabels(map);
                    group.setTofuEntity(tofuEntity);
                    result.add(group);
                }
            }
        }
        return result;
    }

    default List<DriverOptGroup> parseToDriverOptGroupList (JsonNode parameters, TofuEntity tofuEntity) {
        List<DriverOptGroup> result = new ArrayList<>();
        if (parameters.isArray()) {
            for (JsonNode parameter : parameters) {
                if (parameter.isObject()) {
                    Map<String, String> map = new HashMap<>();
                    Iterator<String> fieldNames = parameter.fieldNames();
                    while (fieldNames.hasNext()) {
                        String key = fieldNames.next();
                        String value = parameter.get(key).asText();
                        map.put(key, value);
                    }
                    DriverOptGroup group = new DriverOptGroup();
                    group.setOpts(map);
                    group.setTofuEntity(tofuEntity);
                    result.add(group);
                }
            }
        }
        return result;
    }
}
