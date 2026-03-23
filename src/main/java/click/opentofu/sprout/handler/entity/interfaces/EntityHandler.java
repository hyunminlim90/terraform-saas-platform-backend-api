package click.opentofu.sprout.handler.entity.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.dto.sts.TemporaryCredential;

public interface EntityHandler {
    default void buildEntityAndSaveForSts (JsonNode parameters, TemporaryCredential temporaryCredential) {};
    default void buildEntityAndSave (JsonNode parameters, String resourceSaveName, String authUserIndex, String moduleName) {};
    default void duplicateBuildEntityAndSave (JsonNode parameters, String authUserIndex, String accountId, String region, String moduleName) {};

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

    
}
