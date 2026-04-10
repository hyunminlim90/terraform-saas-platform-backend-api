package click.opentofu.sprout.handler.tofu.module.implementations;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.module.interfaces.ModuleHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("tofu_module_route_table")
public class TofuModuleRouteTable implements ModuleHandler {

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
            .append("module \"aws_route_table\" {\n")
            .append("    source = \"../../tofu_resource/aws_route_table\"\n\n")
            .append("    for_each = local.config.awscloud\n\n")
            .append("    aws_route_table = {\n")
            .append("        for index, aws_route_table in try(each.value.aws_route_table, []) :\n")
            .append("            \"${regex(\"^[a-zA-Z0-9][a-zA-Z0-9._/: -]{1,254}$\", aws_route_table.name)}\" => {\n")

            /** Start of Substitution */

            .append("                vpc_id                     = aws_route_table.vpc_id\n")
            .append("                propagating_vgws           = aws_route_table.propagating_vgws\n")
            .append("                cidr_block                 = aws_route_table.cidr_block\n")
            .append("                ipv6_cidr_block            = aws_route_table.ipv6_cidr_block\n")
            .append("                destination_prefix_list_id = aws_route_table.destination_prefix_list_id\n")
            .append("                carrier_gateway_id         = aws_route_table.carrier_gateway_id\n")
            .append("                core_network_arn           = aws_route_table.core_network_arn\n")
            .append("                egress_only_gateway_id     = aws_route_table.egress_only_gateway_id\n")
            .append("                gateway_id                 = aws_route_table.gateway_id\n")
            .append("                local_gateway_id           = aws_route_table.local_gateway_id\n")
            .append("                nat_gateway_id             = aws_route_table.nat_gateway_id\n")
            .append("                network_interface_id       = aws_route_table.network_interface_id\n")
            .append("                transit_gateway_id         = aws_route_table.transit_gateway_id\n")
            .append("                vpc_endpoint_id            = aws_route_table.vpc_endpoint_id\n")
            .append("                vpc_peering_connection_id  = aws_route_table.vpc_peering_connection_id\n")
            .append("                tags                       = aws_route_table.tags\n")

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
            .append("# https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/route_table#argument-reference\n\n")
            .append("context:\n\n")
            .append("    # Match the 'Uuid of Arn' parameter.\n")
            .append("    name:\n");

        for (Map<String, Object> resourceConfig : resourceConfigs) {
            try {
                String routeTableId = (String) resourceConfig.get("route_table_id");
                if (!routeTableId.contains("create-only-")) {
                    if (routeTableId.startsWith("arn:")) {
                        String resource = routeTableId.split(":", 6)[5];
                        String[] tokens = resource.split("[/:]");
                        routeTableId = String.join("-", tokens);
                    }
                }
                definedPythonFile
                    .append("        - \"" + routeTableId + "\"\n");
            } catch (Exception error) {
                throw new RuntimeException("build_tofu_module_config_file_route_table_id");
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
            .append("    propagating_vgws:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> propagatingVgws = (List<Object>) resourceConfig.get("propagating_vgws");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", propagatingVgws.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_propagating_vgws");
                    }
                }

        definedPythonFile
            .append("    cidr_block:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> cidrBlock = (List<Object>) resourceConfig.get("cidr_block");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", cidrBlock.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_cidr_block");
                    }
                }

        definedPythonFile
            .append("    ipv6_cidr_block:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> ipv6CidrBlock = (List<Object>) resourceConfig.get("ipv6_cidr_block");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", ipv6CidrBlock.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_ipv6_cidr_block");
                    }
                }

        definedPythonFile
            .append("    destination_prefix_list_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> destinationPrefixListId = (List<Object>) resourceConfig.get("destination_prefix_list_id");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", destinationPrefixListId.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_destination_prefix_list_id");
                    }
                }

        definedPythonFile
            .append("    carrier_gateway_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> carrierGatewayId = (List<Object>) resourceConfig.get("carrier_gateway_id");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", carrierGatewayId.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_carrier_gateway_id");
                    }
                }

        definedPythonFile
            .append("    core_network_arn:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> coreNetworkArn = (List<Object>) resourceConfig.get("core_network_arn");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", coreNetworkArn.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_core_network_arn");
                    }
                }

        definedPythonFile
            .append("    egress_only_gateway_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> egressOnlyGatewayId = (List<Object>) resourceConfig.get("egress_only_gateway_id");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", egressOnlyGatewayId.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_egress_only_gateway_id");
                    }
                }

        definedPythonFile
            .append("    gateway_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> gatewayId = (List<Object>) resourceConfig.get("gateway_id");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", gatewayId.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_gateway_id");
                    }
                }

        definedPythonFile
            .append("    local_gateway_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> localGatewayId = (List<Object>) resourceConfig.get("local_gateway_id");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", localGatewayId.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_local_gateway_id");
                    }
                }

        definedPythonFile
            .append("    nat_gateway_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> natGatewayId = (List<Object>) resourceConfig.get("nat_gateway_id");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", natGatewayId.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_nat_gateway_id");
                    }
                }

        definedPythonFile
            .append("    network_interface_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> networkInterfaceId = (List<Object>) resourceConfig.get("network_interface_id");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", networkInterfaceId.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_network_interface_id");
                    }
                }

        definedPythonFile
            .append("    transit_gateway_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> transitGatewayId = (List<Object>) resourceConfig.get("transit_gateway_id");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", transitGatewayId.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_transit_gateway_id");
                    }
                }

        definedPythonFile
            .append("    vpc_endpoint_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> vpcEndpointId = (List<Object>) resourceConfig.get("vpc_endpoint_id");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", vpcEndpointId.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_vpc_endpoint_id");
                    }
                }

        definedPythonFile
            .append("    vpc_peering_connection_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> vpcPeeringConnectionId = (List<Object>) resourceConfig.get("vpc_peering_connection_id");
                        definedPythonFile
                            .append("        - \"" + String.join("{{__,__}}", vpcPeeringConnectionId.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_vpc_peering_connection_id");
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
                .append("        \"aws_route_table\": []\n\n");
        } else {
            definedPythonFile
                .append("        \"aws_route_table\":\n\n");

                    for (int index = 0; index < resourceConfigs.size(); index++) {
                        definedPythonFile
                            .append("            - {\n")
                            .append("                name: \"${name[" + index + "]}\",\n")

                            /** Start of YAML Leaf Node Substitution */

                            .append("                vpc_id: \"${vpc_id[" + index + "]}\",\n")
                            .append("                propagating_vgws: \"${propagating_vgws[" + index + "]}\",\n")
                            .append("                cidr_block: \"${cidr_block[" + index + "]}\",\n")
                            .append("                ipv6_cidr_block: \"${ipv6_cidr_block[" + index + "]}\",\n")
                            .append("                destination_prefix_list_id: \"${destination_prefix_list_id[" + index + "]}\",\n")
                            .append("                carrier_gateway_id: \"${carrier_gateway_id[" + index + "]}\",\n")
                            .append("                core_network_arn: \"${core_network_arn[" + index + "]}\",\n")
                            .append("                egress_only_gateway_id: \"${egress_only_gateway_id[" + index + "]}\",\n")
                            .append("                gateway_id: \"${gateway_id[" + index + "]}\",\n")
                            .append("                local_gateway_id: \"${local_gateway_id[" + index + "]}\",\n")
                            .append("                nat_gateway_id: \"${nat_gateway_id[" + index + "]}\",\n")
                            .append("                network_interface_id: \"${network_interface_id[" + index + "]}\",\n")
                            .append("                transit_gateway_id: \"${transit_gateway_id[" + index + "]}\",\n")
                            .append("                vpc_endpoint_id: \"${vpc_endpoint_id[" + index + "]}\",\n")
                            .append("                vpc_peering_connection_id: \"${vpc_peering_connection_id[" + index + "]}\",\n")

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
            .append("            \"module\": \"module.aws_route_table[\\\"tofu\\\"]\",\n")
            .append("            \"mode\": \"managed\",\n")
            .append("            \"type\": \"aws_route_table\",\n")
            .append("            \"name\": \"product\",\n")
            .append("            \"provider\": \"provider[\\\"registry.terraform.io/hashicorp/aws\\\"]\",\n")
            .append("            \"instances\": [\n");
                for (int index = 0; index < resourceConfigs.size(); index++) {
                    Map<String, Object> resourceConfig = resourceConfigs.get(index);
                    try {

                        /** Generate index_key value */

                        String routeTableId = (String) resourceConfig.get("route_table_id");
                        if (!routeTableId.contains("create-only-")) {
                            if (routeTableId.startsWith("arn:")) {
                                String resource = routeTableId.split(":", 6)[5];
                                String[] tokens = resource.split("[/:]");
                                routeTableId = String.join("-", tokens);
                            }
                        }

                        definedPythonFile
                            .append("                {\n")
                            .append("                    \"index_key\": \"" + routeTableId + "\",\n")
                            .append("                    \"attributes\": {\n");
                            if (!((String) resourceConfig.get("route_table_id")).contains("create-only")) {
                                definedPythonFile
                                    .append("                        \"id\": \"" + resourceConfig.get("route_table_id") + "\"\n");
                            };
                        definedPythonFile
                            .append("                    }\n")
                            .append("                }");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_state_file_route_table_id");
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
