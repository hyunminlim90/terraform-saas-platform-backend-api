package click.opentofu.sprout.handler.tofu.module.implementations;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.module.interfaces.ModuleHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("tofu_module_vpc")
public class TofuModuleVpc implements ModuleHandler {

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
            .append("module \"aws_vpc\" {\n")
            .append("    source = \"../../tofu_resource/aws_vpc\"\n\n")
            .append("    for_each = local.config.awscloud\n\n")
            .append("    aws_vpc = {\n")
            .append("        for index, aws_vpc in try(each.value.aws_vpc, []) :\n")
            .append("            \"${regex(\"^[a-zA-Z0-9][a-zA-Z0-9._/: -]{1,254}$\", aws_vpc.name)}\" => {\n")

            /** Start of Substitution */

            .append("                assign_generated_ipv6_cidr_block     = aws_vpc.assign_generated_ipv6_cidr_block\n")
            .append("                cidr_block                           = aws_vpc.cidr_block\n")
            .append("                enable_dns_hostnames                 = aws_vpc.enable_dns_hostnames\n")
            .append("                enable_dns_support                   = aws_vpc.enable_dns_support\n")
            .append("                enable_network_address_usage_metrics = aws_vpc.enable_network_address_usage_metrics\n")
            .append("                instance_tenancy                     = aws_vpc.instance_tenancy\n")
            .append("                ipv4_ipam_pool_id                    = aws_vpc.ipv4_ipam_pool_id\n")
            .append("                ipv4_netmask_length                  = aws_vpc.ipv4_netmask_length\n")
            .append("                ipv6_cidr_block                      = aws_vpc.ipv6_cidr_block\n")
            .append("                ipv6_cidr_block_network_border_group = aws_vpc.ipv6_cidr_block_network_border_group\n")
            .append("                ipv6_ipam_pool_id                    = aws_vpc.ipv6_ipam_pool_id\n")
            .append("                ipv6_netmask_length                  = aws_vpc.ipv6_netmask_length\n")
            .append("                region                               = aws_vpc.region\n")
            .append("                tags                                 = aws_vpc.tags\n")

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
            .append("# https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/vpc#argument-reference\n\n")
            .append("context:\n\n")
            .append("    # Match the 'Uuid of Arn' parameter.\n")
            .append("    name:\n");

        for (Map<String, Object> resourceConfig : resourceConfigs) {
            try {
                String vpcId = (String) resourceConfig.get("vpc_id");
                if (!vpcId.contains("create-only-")) {
                    if (vpcId.startsWith("arn:")) {
                        String resource = vpcId.split(":", 6)[5];
                        String[] tokens = resource.split("[/:]");
                        vpcId = String.join("-", tokens);
                    }
                }
                definedPythonFile
                    .append("        - \"" + vpcId + "\"\n");
            } catch (Exception error) {
                throw new RuntimeException("build_tofu_module_config_file_vpc_id");
            }
        }

        /** Start of YAML Field Mapping */

        definedPythonFile
            .append("    assign_generated_ipv6_cidr_block:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("assign_generated_ipv6_cidr_block") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_assign_generated_ipv6_cidr_block");
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
            .append("    enable_dns_hostnames:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("enable_dns_hostnames") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_enable_dns_hostnames");
                    }
                }

        definedPythonFile
            .append("    enable_dns_support:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("enable_dns_support") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_enable_dns_support");
                    }
                }
        
        definedPythonFile
            .append("    enable_network_address_usage_metrics:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("enable_network_address_usage_metrics") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_enable_network_address_usage_metrics");
                    }
                }

        definedPythonFile
            .append("    instance_tenancy:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("instance_tenancy") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_instance_tenancy");
                    }
                }
        
        definedPythonFile
            .append("    ipv4_ipam_pool_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("ipv4_ipam_pool_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ipv4_ipam_pool_id");
                    }
                }
        
        definedPythonFile
            .append("    ipv4_netmask_length:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("ipv4_netmask_length") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ipv4_netmask_length");
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
            .append("    ipv6_cidr_block_network_border_group:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("ipv6_cidr_block_network_border_group") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ipv6_cidr_block_network_border_group");
                    }
                }

        definedPythonFile
            .append("    ipv6_ipam_pool_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("ipv6_ipam_pool_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ipv6_ipam_pool_id");
                    }
                }
        
        definedPythonFile
            .append("    ipv6_netmask_length:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("ipv6_netmask_length") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ipv6_netmask_length");
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
                .append("        \"aws_vpc\": []\n\n");
        } else {
            definedPythonFile
                .append("        \"aws_vpc\":\n\n");

                    for (int index = 0; index < resourceConfigs.size(); index++) {
                        definedPythonFile
                            .append("            - {\n")
                            .append("                name: \"${name[" + index + "]}\",\n")

                            /** Start of YAML Leaf Node Substitution */

                            .append("                assign_generated_ipv6_cidr_block: \"${assign_generated_ipv6_cidr_block[" + index + "]}\",\n")
                            .append("                cidr_block: \"${cidr_block[" + index + "]}\",\n")
                            .append("                enable_dns_hostnames: \"${enable_dns_hostnames[" + index + "]}\",\n")
                            .append("                enable_dns_support: \"${enable_dns_support[" + index + "]}\",\n")
                            .append("                enable_network_address_usage_metrics: \"${enable_network_address_usage_metrics[" + index + "]}\",\n")
                            .append("                instance_tenancy: \"${instance_tenancy[" + index + "]}\",\n")
                            .append("                ipv4_ipam_pool_id: \"${ipv4_ipam_pool_id[" + index + "]}\",\n")
                            .append("                ipv4_netmask_length: \"${ipv4_netmask_length[" + index + "]}\",\n")
                            .append("                ipv6_cidr_block: \"${ipv6_cidr_block[" + index + "]}\",\n")
                            .append("                ipv6_cidr_block_network_border_group: \"${ipv6_cidr_block_network_border_group[" + index + "]}\",\n")
                            .append("                ipv6_ipam_pool_id: \"${ipv6_ipam_pool_id[" + index + "]}\",\n")
                            .append("                ipv6_netmask_length: \"${ipv6_netmask_length[" + index + "]}\",\n")
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
            .append("            \"module\": \"module.aws_vpc[\\\"tofu\\\"]\",\n")
            .append("            \"mode\": \"managed\",\n")
            .append("            \"type\": \"aws_vpc\",\n")
            .append("            \"name\": \"product\",\n")
            .append("            \"provider\": \"provider[\\\"registry.terraform.io/hashicorp/aws\\\"]\",\n")
            .append("            \"instances\": [\n");
                for (int index = 0; index < resourceConfigs.size(); index++) {
                    Map<String, Object> resourceConfig = resourceConfigs.get(index);
                    try {

                        /** Generate index_key value */

                        String vpcId = (String) resourceConfig.get("vpc_id");
                        if (!vpcId.contains("create-only-")) {
                            if (vpcId.startsWith("arn:")) {
                                String resource = vpcId.split(":", 6)[5];
                                String[] tokens = resource.split("[/:]");
                                vpcId = String.join("-", tokens);
                            }
                        }

                        definedPythonFile
                            .append("                {\n")
                            .append("                    \"index_key\": \"" + vpcId + "\",\n")
                            .append("                    \"attributes\": {\n");
                            if (!((String) resourceConfig.get("vpc_id")).contains("create-only")) {
                                definedPythonFile
                                    .append("                        \"id\": \"" + resourceConfig.get("vpc_id") + "\"\n");
                            };
                        definedPythonFile
                            .append("                    }\n")
                            .append("                }");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_state_file_vpc_id");
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
