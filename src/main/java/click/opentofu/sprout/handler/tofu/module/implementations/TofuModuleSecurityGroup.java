package click.opentofu.sprout.handler.tofu.module.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.module.interfaces.ModuleHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("tofu_module_security_group")
public class TofuModuleSecurityGroup implements ModuleHandler {

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
            .append("module \"aws_security_group\" {\n")
            .append("    source = \"../../tofu_resource/aws_security_group\"\n\n")
            .append("    for_each = local.config.awscloud\n\n")
            .append("    aws_security_group = {\n")
            .append("        for index, aws_security_group in try(each.value.aws_security_group, []) :\n")
            .append("            \"${regex(\"^[a-zA-Z0-9][a-zA-Z0-9._/: -]{1,254}$\", aws_security_group.name)}\" => {\n")

            /** Start of Substitution */

            .append("                description              = aws_security_group.description\n")
            .append("                security_group_name      = aws_security_group.security_group_name\n")
            .append("                name_prefix              = aws_security_group.name_prefix\n")
            .append("                region                   = aws_security_group.region\n")
            .append("                vpc_id                   = aws_security_group.vpc_id\n")
            .append("                revoke_rules_on_delete   = aws_security_group.revoke_rules_on_delete\n")
            .append("                ingress_protocol         = aws_security_group.ingress_protocol\n")
            .append("                ingress_description      = aws_security_group.ingress_description\n")
            .append("                egress_protocol          = aws_security_group.egress_protocol\n")
            .append("                egress_description       = aws_security_group.egress_description\n")
            .append("                ingress_from_port        = aws_security_group.ingress_from_port\n")
            .append("                ingress_to_port          = aws_security_group.ingress_to_port\n")
            .append("                egress_from_port         = aws_security_group.egress_from_port\n")
            .append("                egress_to_port           = aws_security_group.egress_to_port\n")
            .append("                ingress_self             = aws_security_group.ingress_self\n")
            .append("                egress_self              = aws_security_group.egress_self\n")
            .append("                ingress_cidr_blocks      = aws_security_group.ingress_cidr_blocks\n")
            .append("                ingress_ipv6_cidr_blocks = aws_security_group.ingress_ipv6_cidr_blocks\n")
            .append("                ingress_prefix_list_ids  = aws_security_group.ingress_prefix_list_ids\n")
            .append("                ingress_security_groups  = aws_security_group.ingress_security_groups\n")
            .append("                egress_cidr_blocks       = aws_security_group.egress_cidr_blocks\n")
            .append("                egress_ipv6_cidr_blocks  = aws_security_group.egress_ipv6_cidr_blocks\n")
            .append("                egress_prefix_list_ids   = aws_security_group.egress_prefix_list_ids\n")
            .append("                egress_security_groups   = aws_security_group.egress_security_groups\n")
            .append("                tags                     = aws_security_group.tags\n")

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
            .append("# https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/security_group#argument-reference\n\n")
            .append("context:\n\n")
            .append("    # Match the 'Uuid of Arn' parameter.\n")
            .append("    name:\n");

        for (Map<String, Object> resourceConfig : resourceConfigs) {
            try {
                String securityGroupId = (String) resourceConfig.get("security_group_id");
                if (!securityGroupId.contains("create-only-")) {
                    if (securityGroupId.startsWith("arn:")) {
                        String resource = securityGroupId.split(":", 6)[5];
                        String[] tokens = resource.split("[/:]");
                        securityGroupId = String.join("-", tokens);
                    }
                }
                definedPythonFile
                    .append("        - \"" + securityGroupId + "\"\n");
            } catch (Exception error) {
                throw new RuntimeException("build_tofu_module_config_file_security_group_id");
            }
        }

        /** Start of YAML Field Mapping */

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
            .append("    security_group_name:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("security_group_name") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_security_group_name");
                    }
                }

        definedPythonFile
            .append("    name_prefix:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("name_prefix") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_name_prefix");
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
            .append("    revoke_rules_on_delete:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("revoke_rules_on_delete") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_revoke_rules_on_delete");
                    }
                }

        definedPythonFile
            .append("    ingress_protocol:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ingressProtocol = (List<Object>) resourceConfig.get("ingress_protocol");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ingressProtocol.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ingress_protocol");
                    }
                }

        definedPythonFile
            .append("    ingress_description:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ingressDescription = (List<Object>) resourceConfig.get("ingress_description");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ingressDescription.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ingress_description");
                    }
                }

        definedPythonFile
            .append("    egress_protocol:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> egressProtocol = (List<Object>) resourceConfig.get("egress_protocol");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", egressProtocol.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_egress_protocol");
                    }
                }

        definedPythonFile
            .append("    egress_description:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> egressDescription = (List<Object>) resourceConfig.get("egress_description");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", egressDescription.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_egress_description");
                    }
                }

        definedPythonFile
            .append("    ingress_from_port:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ingressFromPort = (List<Object>) resourceConfig.get("ingress_from_port");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ingressFromPort.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ingress_from_port");
                    }
                }

        definedPythonFile
            .append("    ingress_to_port:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ingressToPort = (List<Object>) resourceConfig.get("ingress_to_port");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ingressToPort.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ingress_to_port");
                    }
                }

        definedPythonFile
            .append("    egress_from_port:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> egressFromPort = (List<Object>) resourceConfig.get("egress_from_port");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", egressFromPort.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_egress_from_port");
                    }
                }

        definedPythonFile
            .append("    egress_to_port:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> egressToPort = (List<Object>) resourceConfig.get("egress_to_port");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", egressToPort.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_egress_to_port");
                    }
                }

        definedPythonFile
            .append("    ingress_self:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ingressSelf = (List<Object>) resourceConfig.get("ingress_self");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ingressSelf.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ingress_self");
                    }
                }

        definedPythonFile
            .append("    egress_self:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> egressSelf = (List<Object>) resourceConfig.get("egress_self");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", egressSelf.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_egress_self");
                    }
                }

        definedPythonFile
            .append("    ingress_cidr_blocks:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<List<String>> ingressCidrBlocks = (List<List<String>>) resourceConfig.get("ingress_cidr_blocks");
                        if (ingressCidrBlocks != null && !ingressCidrBlocks.isEmpty()) {
                            List<String> ingressCidrBlockStrings = new ArrayList<>();
                            for (List<String> item : ingressCidrBlocks) {
                                String joinedInner = String.join("{{__,__}}", item);
                                ingressCidrBlockStrings.add(joinedInner);
                            }
                            definedPythonFile
                                .append("        - \"")
                                .append(String.join("{{__/__}}", ingressCidrBlockStrings))
                                .append("\"\n"); 
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Ingress Cidr Block does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ingress_cidr_blocks");
                    }
                }

        definedPythonFile
            .append("    ingress_ipv6_cidr_blocks:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<List<String>> ingressIpv6CidrBlocks = (List<List<String>>) resourceConfig.get("ingress_ipv6_cidr_blocks");
                        if (ingressIpv6CidrBlocks != null && !ingressIpv6CidrBlocks.isEmpty()) {
                            List<String> ingressIpv6CidrBlockStrings = new ArrayList<>();
                            for (List<String> item : ingressIpv6CidrBlocks) {
                                String joinedInner = String.join("{{__,__}}", item);
                                ingressIpv6CidrBlockStrings.add(joinedInner);
                            }
                            definedPythonFile
                                .append("        - \"")
                                .append(String.join("{{__/__}}", ingressIpv6CidrBlockStrings))
                                .append("\"\n"); 
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Ingress Ipv6 Cidr Block does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ingress_ipv6_cidr_blocks");
                    }
                }

        definedPythonFile
            .append("    ingress_prefix_list_ids:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<List<String>> ingressPrefixListIds = (List<List<String>>) resourceConfig.get("ingress_prefix_list_ids");
                        if (ingressPrefixListIds != null && !ingressPrefixListIds.isEmpty()) {
                            List<String> ingressPrefixListIdStrings = new ArrayList<>();
                            for (List<String> item : ingressPrefixListIds) {
                                String joinedInner = String.join("{{__,__}}", item);
                                ingressPrefixListIdStrings.add(joinedInner);
                            }
                            definedPythonFile
                                .append("        - \"")
                                .append(String.join("{{__/__}}", ingressPrefixListIdStrings))
                                .append("\"\n"); 
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Ingress Prefix List Id does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ingress_prefix_list_ids");
                    }
                }

        definedPythonFile
            .append("    ingress_security_groups:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<List<String>> ingressSecurityGroups = (List<List<String>>) resourceConfig.get("ingress_security_groups");
                        if (ingressSecurityGroups != null && !ingressSecurityGroups.isEmpty()) {
                            List<String> ingressSecurityGroupStrings = new ArrayList<>();
                            for (List<String> item : ingressSecurityGroups) {
                                String joinedInner = String.join("{{__,__}}", item);
                                ingressSecurityGroupStrings.add(joinedInner);
                            }
                            definedPythonFile
                                .append("        - \"")
                                .append(String.join("{{__/__}}", ingressSecurityGroupStrings))
                                .append("\"\n"); 
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Ingress Security Group does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ingress_security_groups");
                    }
                }

        definedPythonFile
            .append("    egress_cidr_blocks:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<List<String>> egressCidrBlocks = (List<List<String>>) resourceConfig.get("egress_cidr_blocks");
                        if (egressCidrBlocks != null && !egressCidrBlocks.isEmpty()) {
                            List<String> egressCidrBlockStrings = new ArrayList<>();
                            for (List<String> item : egressCidrBlocks) {
                                String joinedInner = String.join("{{__,__}}", item);
                                egressCidrBlockStrings.add(joinedInner);
                            }
                            definedPythonFile
                                .append("        - \"")
                                .append(String.join("{{__/__}}", egressCidrBlockStrings))
                                .append("\"\n"); 
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Egress Cidr Block does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_egress_cidr_blocks");
                    }
                }

        definedPythonFile
            .append("    egress_ipv6_cidr_blocks:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<List<String>> egressIpv6CidrBlocks = (List<List<String>>) resourceConfig.get("egress_ipv6_cidr_blocks");
                        if (egressIpv6CidrBlocks != null && !egressIpv6CidrBlocks.isEmpty()) {
                            List<String> egressIpv6CidrBlockStrings = new ArrayList<>();
                            for (List<String> item : egressIpv6CidrBlocks) {
                                String joinedInner = String.join("{{__,__}}", item);
                                egressIpv6CidrBlockStrings.add(joinedInner);
                            }
                            definedPythonFile
                                .append("        - \"")
                                .append(String.join("{{__/__}}", egressIpv6CidrBlockStrings))
                                .append("\"\n"); 
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Egress Ipv6 Cidr Block does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_egress_ipv6_cidr_blocks");
                    }
                }

        definedPythonFile
            .append("    egress_prefix_list_ids:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<List<String>> egressPrefixListIds = (List<List<String>>) resourceConfig.get("egress_prefix_list_ids");
                        if (egressPrefixListIds != null && !egressPrefixListIds.isEmpty()) {
                            List<String> egressPrefixListIdStrings = new ArrayList<>();
                            for (List<String> item : egressPrefixListIds) {
                                String joinedInner = String.join("{{__,__}}", item);
                                egressPrefixListIdStrings.add(joinedInner);
                            }
                            definedPythonFile
                                .append("        - \"")
                                .append(String.join("{{__/__}}", egressPrefixListIdStrings))
                                .append("\"\n"); 
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Egress Prefix List Id does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_egress_prefix_list_ids");
                    }
                }

        definedPythonFile
            .append("    egress_security_groups:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<List<String>> egressSecurityGroups = (List<List<String>>) resourceConfig.get("egress_security_groups");
                        if (egressSecurityGroups != null && !egressSecurityGroups.isEmpty()) {
                            List<String> egressSecurityGroupStrings = new ArrayList<>();
                            for (List<String> item : egressSecurityGroups) {
                                String joinedInner = String.join("{{__,__}}", item);
                                egressSecurityGroupStrings.add(joinedInner);
                            }
                            definedPythonFile
                                .append("        - \"")
                                .append(String.join("{{__/__}}", egressSecurityGroupStrings))
                                .append("\"\n"); 
                        } else {
                            definedPythonFile
                                .append("        - \"\"\n");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                            log.warn("Egress Security Group does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                            log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_egress_security_groups");
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
                .append("        \"aws_security_group\": []\n\n");
        } else {
            definedPythonFile
                .append("        \"aws_security_group\":\n\n");

                    for (int index = 0; index < resourceConfigs.size(); index++) {
                        definedPythonFile
                            .append("            - {\n")
                            .append("                name: \"${name[" + index + "]}\",\n")

                            /** Start of YAML Leaf Node Substitution */

                            .append("                description: \"${description[" + index + "]}\",\n")
                            .append("                security_group_name: \"${security_group_name[" + index + "]}\",\n")
                            .append("                name_prefix: \"${name_prefix[" + index + "]}\",\n")
                            .append("                region: \"${region[" + index + "]}\",\n")
                            .append("                vpc_id: \"${vpc_id[" + index + "]}\",\n")
                            .append("                revoke_rules_on_delete: \"${revoke_rules_on_delete[" + index + "]}\",\n")
                            .append("                ingress_protocol: \"${ingress_protocol[" + index + "]}\",\n")
                            .append("                ingress_description: \"${ingress_description[" + index + "]}\",\n")
                            .append("                egress_protocol: \"${egress_protocol[" + index + "]}\",\n")
                            .append("                egress_description: \"${egress_description[" + index + "]}\",\n")
                            .append("                ingress_from_port: \"${ingress_from_port[" + index + "]}\",\n")
                            .append("                ingress_to_port: \"${ingress_to_port[" + index + "]}\",\n")
                            .append("                egress_from_port: \"${egress_from_port[" + index + "]}\",\n")
                            .append("                egress_to_port: \"${egress_to_port[" + index + "]}\",\n")
                            .append("                ingress_self: \"${ingress_self[" + index + "]}\",\n")
                            .append("                egress_self: \"${egress_self[" + index + "]}\",\n")
                            .append("                ingress_cidr_blocks: \"${ingress_cidr_blocks[" + index + "]}\",\n")
                            .append("                ingress_ipv6_cidr_blocks: \"${ingress_ipv6_cidr_blocks[" + index + "]}\",\n")
                            .append("                ingress_prefix_list_ids: \"${ingress_prefix_list_ids[" + index + "]}\",\n")
                            .append("                ingress_security_groups: \"${ingress_security_groups[" + index + "]}\",\n")
                            .append("                egress_cidr_blocks: \"${egress_cidr_blocks[" + index + "]}\",\n")
                            .append("                egress_ipv6_cidr_blocks: \"${egress_ipv6_cidr_blocks[" + index + "]}\",\n")
                            .append("                egress_prefix_list_ids: \"${egress_prefix_list_ids[" + index + "]}\",\n")
                            .append("                egress_security_groups: \"${egress_security_groups[" + index + "]}\",\n")

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
            .append("            \"module\": \"module.aws_security_group[\\\"tofu\\\"]\",\n")
            .append("            \"mode\": \"managed\",\n")
            .append("            \"type\": \"aws_security_group\",\n")
            .append("            \"name\": \"product\",\n")
            .append("            \"provider\": \"provider[\\\"registry.terraform.io/hashicorp/aws\\\"]\",\n")
            .append("            \"instances\": [\n");
                for (int index = 0; index < resourceConfigs.size(); index++) {
                    Map<String, Object> resourceConfig = resourceConfigs.get(index);
                    try {

                        /** Generate index_key value */

                        String securityGroupId = (String) resourceConfig.get("security_group_id");
                        if (!securityGroupId.contains("create-only-")) {
                            if (securityGroupId.startsWith("arn:")) {
                                String resource = securityGroupId.split(":", 6)[5];
                                String[] tokens = resource.split("[/:]");
                                securityGroupId = String.join("-", tokens);
                            }
                        }

                        definedPythonFile
                            .append("                {\n")
                            .append("                    \"index_key\": \"" + securityGroupId + "\",\n")
                            .append("                    \"attributes\": {\n");
                            if (!((String) resourceConfig.get("security_group_id")).contains("create-only")) {
                                definedPythonFile
                                    .append("                        \"id\": \"" + resourceConfig.get("security_group_id") + "\"\n");
                            };
                        definedPythonFile
                            .append("                    }\n")
                            .append("                }");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_state_file_security_group_id");
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
