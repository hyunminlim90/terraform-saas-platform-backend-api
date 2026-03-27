package click.opentofu.sprout.handler.tofu.module.implementations;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.module.interfaces.ModuleHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("tofu_module_vpc_ipam_pool")
public class TofuModuleVpcIpamPool implements ModuleHandler {

    @Override
    public String buildTofuModuleMainFile(String region, String access_key, String secret_key, String token) {
        StringBuilder definedPythonFile = new StringBuilder();
        definedPythonFile
            .append("provider \"aws\" {\n")
            .append("    region     = \"" + region + "\"\n")
            .append("    access_key = \"" + access_key + "\"\n")
            .append("    secret_key = \"" + secret_key + "\"\n")
            .append("    token      = \"" + token + "\"\n")
            .append("}\n\n")
            .append("locals {\n")
            .append("    context = yamldecode(file(\"./config.yaml\")).context\n")
            .append("    config  = yamldecode(templatefile(\"./config.yaml\", local.context))\n")
            .append("}\n\n")
            /**
             * 
             * 디버깅 시 locals.config 도 주석 처리 필요.
             */
            // .append("output \"debug\" {\n")
            // .append("    value = templatefile(\"./config.yaml\", local.context)\n")
            // .append("}\n\n")
            .append("module \"aws_vpc_ipam_pool\" {\n")
            .append("    source = \"../../tofu_resource/aws_vpc_ipam_pool\"\n\n")
            .append("    for_each = local.config.awscloud\n\n")
            .append("    aws_vpc_ipam_pool = {\n")
            .append("        for index, aws_vpc_ipam_pool in try(each.value.aws_vpc_ipam_pool, []) :\n")
            .append("            \"${regex(\"^[a-zA-Z0-9][a-zA-Z0-9._/: -]{1,254}$\", aws_vpc_ipam_pool.name)}\" => {\n")

            /** Start of Substitution */

            .append("                address_family                    = aws_vpc_ipam_pool.address_family\n")
            .append("                aws_service                       = aws_vpc_ipam_pool.aws_service\n")
            .append("                description                       = aws_vpc_ipam_pool.description\n")
            .append("                ipam_scope_id                     = aws_vpc_ipam_pool.ipam_scope_id\n")
            .append("                locale                            = aws_vpc_ipam_pool.locale\n")
            .append("                public_ip_source                  = aws_vpc_ipam_pool.public_ip_source\n")
            .append("                region                            = aws_vpc_ipam_pool.region\n")
            .append("                source_ipam_pool_id               = aws_vpc_ipam_pool.source_ipam_pool_id\n")
            .append("                allocation_default_netmask_length = aws_vpc_ipam_pool.allocation_default_netmask_length\n")
            .append("                allocation_max_netmask_length     = aws_vpc_ipam_pool.allocation_max_netmask_length\n")
            .append("                allocation_min_netmask_length     = aws_vpc_ipam_pool.allocation_min_netmask_length\n")
            .append("                auto_import                       = aws_vpc_ipam_pool.auto_import\n")
            .append("                cascade                           = aws_vpc_ipam_pool.cascade\n")
            .append("                publicly_advertisable             = aws_vpc_ipam_pool.publicly_advertisable\n")
            .append("                allocation_resource_tags          = aws_vpc_ipam_pool.allocation_resource_tags\n")
            .append("                tags                              = aws_vpc_ipam_pool.tags\n")

            /** End of Substitution */

            .append("            }\n")
            .append("    }\n")
            .append("}\n");
        return definedPythonFile.toString();
    }

    @Override
    public String buildTofuModuleConfigFile(List<Map<String, Object>> resourceConfigs, String operation) {
        StringBuilder definedPythonFile = new StringBuilder();
        definedPythonFile
            .append("# https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/vpc_ipam_pool#argument-reference\n\n")
            .append("context:\n\n")
            .append("    # Match the 'Uuid of Arn' parameter.\n")
            .append("    name:\n");

        for (Map<String, Object> resourceConfig : resourceConfigs) {
            try {
                String vpcIpamPoolId = (String) resourceConfig.get("vpc_ipam_pool_id");
                if (!vpcIpamPoolId.contains("create-only-")) {
                    if (vpcIpamPoolId.startsWith("arn:")) {
                        String resource = vpcIpamPoolId.split(":", 6)[5];
                        String[] tokens = resource.split("[/:]");
                        vpcIpamPoolId = String.join("-", tokens);
                    }
                }
                definedPythonFile
                    .append("        - \"" + vpcIpamPoolId + "\"\n");
            } catch (Exception error) {
                throw new RuntimeException("build_tofu_module_config_file_id");
            }
        }

        /** Start of YAML Field Mapping */

        definedPythonFile
            .append("    address_family:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("address_family") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_address_family");
                    }
                }

        definedPythonFile
            .append("    aws_service:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("aws_service") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_aws_service");
                    }
                }

        definedPythonFile
            .append("    description:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("description") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_description");
                    }
                }

        definedPythonFile
            .append("    ipam_scope_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("ipam_scope_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ipam_scope_id");
                    }
                }

        definedPythonFile
            .append("    locale:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("locale") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_locale");
                    }
                }

        definedPythonFile
            .append("    public_ip_source:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("public_ip_source") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_public_ip_source");
                    }
                }

        definedPythonFile
            .append("    region:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("region") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_region");
                    }
                }

        definedPythonFile
            .append("    source_ipam_pool_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("source_ipam_pool_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_source_ipam_pool_id");
                    }
                }

        definedPythonFile
            .append("    allocation_default_netmask_length:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("allocation_default_netmask_length") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_allocation_default_netmask_length");
                    }
                }

        definedPythonFile
            .append("    allocation_max_netmask_length:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("allocation_max_netmask_length") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_allocation_max_netmask_length");
                    }
                }

        definedPythonFile
            .append("    allocation_min_netmask_length:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("allocation_min_netmask_length") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_allocation_min_netmask_length");
                    }
                }

        definedPythonFile
            .append("    auto_import:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("auto_import") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_auto_import");
                    }
                }

        definedPythonFile
            .append("    cascade:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("cascade") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_cascade");
                    }
                }

        definedPythonFile
            .append("    publicly_advertisable:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("publicly_advertisable") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_publicly_advertisable");
                    }
                }

        definedPythonFile
            .append("    allocation_resource_tags:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> allocationResourceTags = (Map<String, Object>) resourceConfig.get("allocation_resource_tags");
                        if (allocationResourceTags != null && !allocationResourceTags.isEmpty()) {
                            String allocationResourceTagResult = allocationResourceTags.entrySet().stream() 
                                .map((entry) -> { return entry.getKey() + "{{__:__}}" + entry.getValue(); })
                                .collect(Collectors.joining("{{__,__}}"));
                            definedPythonFile.append("        - \"" + allocationResourceTagResult + "\"\n");
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Allocation Resource Tag does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_allocation_resource_tags");
                    }
                }

        /** End of YAML Field Mapping */

        definedPythonFile
            .append("    tags:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> tags = (Map<String, Object>) resourceConfig.get("tags");
                        if (tags != null && !tags.isEmpty()) {
                            String tagResult = tags.entrySet().stream() 
                                .map((entry) -> { return entry.getKey() + "{{__:__}}" + entry.getValue(); })
                                .collect(Collectors.joining("{{__,__}}"));
                            definedPythonFile.append("        - \"" + tagResult + "\"\n");
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Tag does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_tags");
                    }
                }
        definedPythonFile
            .append("\n")
            .append("awscloud:\n")
            .append("    \"tofu\":\n");
        if (operation != null && !operation.isBlank() && operation.contains("destroy")) {
            definedPythonFile
                .append("        \"aws_vpc_ipam_pool\": []\n\n");
        } else {
            definedPythonFile
                .append("        \"aws_vpc_ipam_pool\":\n\n");

                    for (int index = 0; index < resourceConfigs.size(); index++) {
                        definedPythonFile
                            .append("            - {\n")
                            .append("                name: \"${name[" + index + "]}\",\n")

                            /** Start of YAML Leaf Node Substitution */

                            .append("                address_family: \"${address_family[" + index + "]}\",\n")
                            .append("                aws_service: \"${aws_service[" + index + "]}\",\n")
                            .append("                description: \"${description[" + index + "]}\",\n")
                            .append("                ipam_scope_id: \"${ipam_scope_id[" + index + "]}\",\n")
                            .append("                locale: \"${locale[" + index + "]}\",\n")
                            .append("                public_ip_source: \"${public_ip_source[" + index + "]}\",\n")
                            .append("                region: \"${region[" + index + "]}\",\n")
                            .append("                source_ipam_pool_id: \"${source_ipam_pool_id[" + index + "]}\",\n")
                            .append("                allocation_default_netmask_length: \"${allocation_default_netmask_length[" + index + "]}\",\n")
                            .append("                allocation_max_netmask_length: \"${allocation_max_netmask_length[" + index + "]}\",\n")
                            .append("                allocation_min_netmask_length: \"${allocation_min_netmask_length[" + index + "]}\",\n")
                            .append("                auto_import: \"${auto_import[" + index + "]}\",\n")
                            .append("                cascade: \"${cascade[" + index + "]}\",\n")
                            .append("                publicly_advertisable: \"${publicly_advertisable[" + index + "]}\",\n")
                            .append("                allocation_resource_tags: \"${allocation_resource_tags[" + index + "]}\",\n")

                            /** End of YAML Leaf Node Substitution */

                            .append("                tags: \"${tags[" + index + "]}\"\n")
                            .append("            }\n");
                    }
        }
        return definedPythonFile.toString();
    }

    @Override
    public String buildTofuModuleStateFile(List<Map<String, Object>> resourceConfigs) {
        StringBuilder definedPythonFile = new StringBuilder();
        definedPythonFile
            .append("{\n")
            .append("    \"version\": 4,\n")
            .append("    \"resources\": [\n")
            .append("        {\n")
            .append("            \"module\": \"module.aws_vpc_ipam_pool[\\\"tofu\\\"]\",\n")
            .append("            \"mode\": \"managed\",\n")
            .append("            \"type\": \"aws_vpc_ipam_pool\",\n")
            .append("            \"name\": \"product\",\n")
            .append("            \"provider\": \"provider[\\\"registry.terraform.io/hashicorp/aws\\\"]\",\n")
            .append("            \"instances\": [\n");
                for (int index = 0; index < resourceConfigs.size(); index++) {
                    Map<String, Object> resourceConfig = resourceConfigs.get(index);
                    try {

                        /** Generate index_key value */

                        String vpcIpamPoolId = (String) resourceConfig.get("vpc_ipam_pool_id");
                        if (!vpcIpamPoolId.contains("create-only-")) {
                            if (vpcIpamPoolId.startsWith("arn:")) {
                                String resource = vpcIpamPoolId.split(":", 6)[5];
                                String[] tokens = resource.split("[/:]");
                                vpcIpamPoolId = String.join("-", tokens);
                            }
                        }

                        definedPythonFile
                            .append("                {\n")
                            .append("                    \"index_key\": \"" + vpcIpamPoolId + "\",\n")
                            .append("                    \"attributes\": {\n");
                            if (!((String) resourceConfig.get("vpc_ipam_pool_id")).contains("create-only")) {
                                definedPythonFile
                                    .append("                        \"id\": \"" + resourceConfig.get("vpc_ipam_pool_id") + "\"\n");
                            };
                        definedPythonFile
                            .append("                    }\n")
                            .append("                }");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_state_file_vpc_ipam_pool_id");
                    }
                    if (index < resourceConfigs.size() - 1) {
                        definedPythonFile.append(",");
                    }
                    definedPythonFile.append("\n");
                }
        definedPythonFile
            .append("            ]\n")
            .append("        }\n")
            .append("    ]\n")
            .append("}\n");
        return definedPythonFile.toString();
    }

    @Override
    public String buildTofuTextareaFile(String textarea) {
        StringBuilder definedPythonFile = new StringBuilder();
        definedPythonFile
            .append(textarea);
        return definedPythonFile.toString();
    }
}
