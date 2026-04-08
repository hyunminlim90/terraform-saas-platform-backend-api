package click.opentofu.sprout.handler.tofu.module.implementations;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.module.interfaces.ModuleHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("tofu_module_subnet")
public class TofuModuleSubnet implements ModuleHandler {

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
            .append("module \"aws_subnet\" {\n")
            .append("    source = \"../../tofu_resource/aws_subnet\"\n\n")
            .append("    for_each = local.config.awscloud\n\n")
            .append("    aws_subnet = {\n")
            .append("        for index, aws_subnet in try(each.value.aws_subnet, []) :\n")
            .append("            \"${regex(\"^[a-zA-Z0-9][a-zA-Z0-9._/: -]{1,254}$\", aws_subnet.name)}\" => {\n")

            /** Start of Substitution */

            .append("                vpc_id                                         = aws_subnet.vpc_id\n")
            .append("                assign_ipv6_address_on_creation                = aws_subnet.assign_ipv6_address_on_creation\n")
            .append("                availability_zone                              = aws_subnet.availability_zone\n")
            .append("                availability_zone_id                           = aws_subnet.availability_zone_id\n")
            .append("                cidr_block                                     = aws_subnet.cidr_block\n")
            .append("                customer_owned_ipv4_pool                       = aws_subnet.customer_owned_ipv4_pool\n")
            .append("                enable_dns64                                   = aws_subnet.enable_dns64\n")
            .append("                enable_lni_at_device_index                     = aws_subnet.enable_lni_at_device_index\n")
            .append("                enable_resource_name_dns_a_record_on_launch    = aws_subnet.enable_resource_name_dns_a_record_on_launch\n")
            .append("                enable_resource_name_dns_aaaa_record_on_launch = aws_subnet.enable_resource_name_dns_aaaa_record_on_launch\n")
            .append("                ipv6_cidr_block                                = aws_subnet.ipv6_cidr_block\n")
            .append("                ipv6_native                                    = aws_subnet.ipv6_native\n")
            .append("                map_customer_owned_ip_on_launch                = aws_subnet.map_customer_owned_ip_on_launch\n")
            .append("                map_public_ip_on_launch                        = aws_subnet.map_public_ip_on_launch\n")
            .append("                outpost_arn                                    = aws_subnet.outpost_arn\n")
            .append("                private_dns_hostname_type_on_launch            = aws_subnet.private_dns_hostname_type_on_launch\n")
            .append("                region                                         = aws_subnet.region\n")
            .append("                tags                                           = aws_subnet.tags\n")

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
            .append("# https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/subnet#argument-reference\n\n")
            .append("context:\n\n")
            .append("    # Match the 'Uuid of Arn' parameter.\n")
            .append("    name:\n");

        for (Map<String, Object> resourceConfig : resourceConfigs) {
            try {
                String subnetId = (String) resourceConfig.get("subnet_id");
                if (!subnetId.contains("create-only-")) {
                    if (subnetId.startsWith("arn:")) {
                        String resource = subnetId.split(":", 6)[5];
                        String[] tokens = resource.split("[/:]");
                        subnetId = String.join("-", tokens);
                    }
                }
                definedPythonFile
                    .append("        - \"" + subnetId + "\"\n");
            } catch (Exception error) {
                throw new RuntimeException("build_tofu_module_config_file_subnet_id");
            }
        }

        /** Start of YAML Field Mapping */

        definedPythonFile
            .append("    vpc_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("vpc_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_vpc_id");
                    }
                }

        definedPythonFile
            .append("    assign_ipv6_address_on_creation:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("assign_ipv6_address_on_creation") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_assign_ipv6_address_on_creation");
                    }
                }

        definedPythonFile
            .append("    availability_zone:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("availability_zone") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_availability_zone");
                    }
                }

        definedPythonFile
            .append("    availability_zone_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("availability_zone_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_availability_zone_id");
                    }
                }

        definedPythonFile
            .append("    cidr_block:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("cidr_block") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_cidr_block");
                    }
                }

        definedPythonFile
            .append("    customer_owned_ipv4_pool:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("customer_owned_ipv4_pool") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_customer_owned_ipv4_pool");
                    }
                }

        definedPythonFile
            .append("    enable_dns64:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("enable_dns64") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_enable_dns64");
                    }
                }

        definedPythonFile
            .append("    enable_lni_at_device_index:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("enable_lni_at_device_index") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_enable_lni_at_device_index");
                    }
                }

        definedPythonFile
            .append("    enable_resource_name_dns_a_record_on_launch:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("enable_resource_name_dns_a_record_on_launch") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_enable_resource_name_dns_a_record_on_launch");
                    }
                }

        definedPythonFile
            .append("    enable_resource_name_dns_aaaa_record_on_launch:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("enable_resource_name_dns_aaaa_record_on_launch") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_enable_resource_name_dns_aaaa_record_on_launch");
                    }
                }

        definedPythonFile
            .append("    ipv6_cidr_block:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("ipv6_cidr_block") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ipv6_cidr_block");
                    }
                }

        definedPythonFile
            .append("    ipv6_native:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("ipv6_native") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ipv6_native");
                    }
                }

        definedPythonFile
            .append("    map_customer_owned_ip_on_launch:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("map_customer_owned_ip_on_launch") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_map_customer_owned_ip_on_launch");
                    }
                }

        definedPythonFile
            .append("    map_public_ip_on_launch:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("map_public_ip_on_launch") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_map_public_ip_on_launch");
                    }
                }

        definedPythonFile
            .append("    outpost_arn:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("outpost_arn") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_outpost_arn");
                    }
                }

        definedPythonFile
            .append("    private_dns_hostname_type_on_launch:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("private_dns_hostname_type_on_launch") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_private_dns_hostname_type_on_launch");
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
                .append("        \"aws_subnet\": []\n\n");
        } else {
            definedPythonFile
                .append("        \"aws_subnet\":\n\n");

                    for (int index = 0; index < resourceConfigs.size(); index++) {
                        definedPythonFile
                            .append("            - {\n")
                            .append("                name: \"${name[" + index + "]}\",\n")

                            /** Start of YAML Leaf Node Substitution */

                            .append("                vpc_id: \"${vpc_id[" + index + "]}\",\n")
                            .append("                assign_ipv6_address_on_creation: \"${assign_ipv6_address_on_creation[" + index + "]}\",\n")
                            .append("                availability_zone: \"${availability_zone[" + index + "]}\",\n")
                            .append("                availability_zone_id: \"${availability_zone_id[" + index + "]}\",\n")
                            .append("                cidr_block: \"${cidr_block[" + index + "]}\",\n")
                            .append("                customer_owned_ipv4_pool: \"${customer_owned_ipv4_pool[" + index + "]}\",\n")
                            .append("                enable_dns64: \"${enable_dns64[" + index + "]}\",\n")
                            .append("                enable_lni_at_device_index: \"${enable_lni_at_device_index[" + index + "]}\",\n")
                            .append("                enable_resource_name_dns_a_record_on_launch: \"${enable_resource_name_dns_a_record_on_launch[" + index + "]}\",\n")
                            .append("                enable_resource_name_dns_aaaa_record_on_launch: \"${enable_resource_name_dns_aaaa_record_on_launch[" + index + "]}\",\n")
                            .append("                ipv6_cidr_block: \"${ipv6_cidr_block[" + index + "]}\",\n")
                            .append("                ipv6_native: \"${ipv6_native[" + index + "]}\",\n")
                            .append("                map_customer_owned_ip_on_launch: \"${map_customer_owned_ip_on_launch[" + index + "]}\",\n")
                            .append("                map_public_ip_on_launch: \"${map_public_ip_on_launch[" + index + "]}\",\n")
                            .append("                outpost_arn: \"${outpost_arn[" + index + "]}\",\n")
                            .append("                private_dns_hostname_type_on_launch: \"${private_dns_hostname_type_on_launch[" + index + "]}\",\n")
                            .append("                region: \"${region[" + index + "]}\",\n")

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
            .append("            \"module\": \"module.aws_subnet[\\\"tofu\\\"]\",\n")
            .append("            \"mode\": \"managed\",\n")
            .append("            \"type\": \"aws_subnet\",\n")
            .append("            \"name\": \"product\",\n")
            .append("            \"provider\": \"provider[\\\"registry.terraform.io/hashicorp/aws\\\"]\",\n")
            .append("            \"instances\": [\n");
                for (int index = 0; index < resourceConfigs.size(); index++) {
                    Map<String, Object> resourceConfig = resourceConfigs.get(index);
                    try {

                        /** Generate index_key value */

                        String subnetId = (String) resourceConfig.get("subnet_id");
                        if (!subnetId.contains("create-only-")) {
                            if (subnetId.startsWith("arn:")) {
                                String resource = subnetId.split(":", 6)[5];
                                String[] tokens = resource.split("[/:]");
                                subnetId = String.join("-", tokens);
                            }
                        }

                        definedPythonFile
                            .append("                {\n")
                            .append("                    \"index_key\": \"" + subnetId + "\",\n")
                            .append("                    \"attributes\": {\n");
                            if (!((String) resourceConfig.get("subnet_id")).contains("create-only")) {
                                definedPythonFile
                                    .append("                        \"id\": \"" + resourceConfig.get("subnet_id") + "\"\n");
                            };
                        definedPythonFile
                            .append("                    }\n")
                            .append("                }");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_state_file_subnet_id");
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
