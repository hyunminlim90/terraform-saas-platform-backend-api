package click.opentofu.sprout.handler.entity.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import click.opentofu.sprout.dto.sts.TemporaryCredential;
import click.opentofu.sprout.handler.entity.entity.instance.entity.InstanceEntity;
import click.opentofu.sprout.handler.entity.entity.instance.relation.EbsDevTagGroup;
import click.opentofu.sprout.handler.entity.entity.instance.relation.PrivateIpAddressGroup;

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

        /** vpc_ipam_pool */

    default Map<String, String> parseAllocationResourceTags (JsonNode allocationResourceTagsNode) {
        Map<String, String> allocationResourceTagsMap = new HashMap<>();
        if (!allocationResourceTagsNode.isMissingNode()) {
            for (Map.Entry<String, JsonNode> entry : allocationResourceTagsNode.properties()) {
                allocationResourceTagsMap.put(entry.getKey(), entry.getValue().asText());
            }
        }
        return allocationResourceTagsMap;
    }

        /** instance */

    default Map<String, String> parseRootDevTags (JsonNode rootDevTagsNode) {
        Map<String, String> rootDevTagsMap = new HashMap<>();
        if (!rootDevTagsNode.isMissingNode()) {
            for (Map.Entry<String, JsonNode> entry : rootDevTagsNode.properties()) {
                rootDevTagsMap.put(entry.getKey(), entry.getValue().asText());
            }
        }
        return rootDevTagsMap;
    }

    default Map<String, String> parseVolumeTags (JsonNode volumeTagsNode) {
        Map<String, String> volumeTagsMap = new HashMap<>();
        if (!volumeTagsNode.isMissingNode()) {
            for (Map.Entry<String, JsonNode> entry : volumeTagsNode.properties()) {
                volumeTagsMap.put(entry.getKey(), entry.getValue().asText());
            }
        }
        return volumeTagsMap;
    }

    default List<EbsDevTagGroup> parseToEbsDevTagGroupList (JsonNode parameters, InstanceEntity instanceEntity) {
        List<EbsDevTagGroup> result = new ArrayList<>();
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
                    EbsDevTagGroup group = new EbsDevTagGroup();
                    group.setEbsDevTags(map);
                    group.setInstanceEntity(instanceEntity);
                    result.add(group);
                }
            }
        }
        return result;
    }

    default List<PrivateIpAddressGroup> parseToPrivateIpAddressGroupList (JsonNode parameters, InstanceEntity instanceEntity) {
        List<PrivateIpAddressGroup> result = new ArrayList<>();
        if (parameters.isArray()) {
            for (JsonNode parameter : parameters) {
                if (parameter.isArray()) {
                    List<String> list = new ArrayList<>();
                    for (JsonNode element : parameter) {
                        list.add(element.asText());
                    }
                    PrivateIpAddressGroup group = new PrivateIpAddressGroup();
                    group.setPrivateIpAddresses(list);
                    group.setInstanceEntity(instanceEntity);
                    result.add(group);
                }
            }
        }
        return result;
    }
}
