package click.opentofu.sprout.handler.tofu.module.implementations;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.module.interfaces.ModuleHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("tofu_module_route_table_association")
public class TofuModuleRouteTableAssociation implements ModuleHandler {

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
            .append("module \"aws_route_table_association\" {\n")
            .append("    source = \"../../tofu_resource/aws_route_table_association\"\n\n")
            .append("    for_each = local.config.awscloud\n\n")
            .append("    aws_route_table_association = {\n")
            .append("        for index, aws_route_table_association in try(each.value.aws_route_table_association, []) :\n")
            .append("            \"${regex(\"^[a-zA-Z0-9][a-zA-Z0-9._/: -]{1,254}$\", aws_route_table_association.name)}\" => {\n")

            /** Start of Substitution */

            .append("                gateway_id     = aws_route_table_association.gateway_id\n")
            .append("                route_table_id = aws_route_table_association.route_table_id\n")
            .append("                subnet_id      = aws_route_table_association.subnet_id\n")

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
            .append("# https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/route_table_association#argument-reference\n\n")
            .append("context:\n\n")
            .append("    # Match the 'Uuid of Arn' parameter.\n")
            .append("    name:\n");

        for (Map<String, Object> resourceConfig : resourceConfigs) {
            try {
                String routeTableAssociationId = (String) resourceConfig.get("route_table_association_id");
                if (!routeTableAssociationId.contains("create-only-")) {
                    if (routeTableAssociationId.startsWith("arn:")) {
                        String resource = routeTableAssociationId.split(":", 6)[5];
                        String[] tokens = resource.split("[/:]");
                        routeTableAssociationId = String.join("-", tokens);
                    }
                }
                definedPythonFile
                    .append("        - \"" + routeTableAssociationId + "\"\n");
            } catch (Exception error) {
                throw new RuntimeException("build_tofu_module_config_file_route_table_association_id");
            }
        }

        /** Start of YAML Field Mapping */

        definedPythonFile
            .append("    gateway_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("gateway_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_gateway_id");
                    }
                }

        definedPythonFile
            .append("    route_table_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("route_table_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_route_table_id");
                    }
                }

        definedPythonFile
            .append("    subnet_id:\n");
                for (Map<String, Object> resourceConfig : resourceConfigs) {
                    try {
                        definedPythonFile
                            .append("        - \"" + resourceConfig.get("subnet_id") + "\"\n");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_config_file_subnet_id");
                    }
                }
        
        /** End of YAML Field Mapping */

        definedPythonFile
            .append("\n")
            .append("awscloud:\n")
            .append("    \"tofu\":\n");
        if (operation != null && !operation.isBlank() && operation.contains("destroy")) {
            definedPythonFile
                .append("        \"aws_route_table_association\": []\n\n");
        } else {
            definedPythonFile
                .append("        \"aws_route_table_association\":\n\n");

                    for (int index = 0; index < resourceConfigs.size(); index++) {
                        definedPythonFile
                            .append("            - {\n")
                            .append("                name: \"${name[" + index + "]}\",\n")

                            /** Start of YAML Leaf Node Substitution */

                            .append("                gateway_id: \"${gateway_id[" + index + "]}\",\n")
                            .append("                route_table_id: \"${route_table_id[" + index + "]}\",\n")
                            .append("                subnet_id: \"${subnet_id[" + index + "]}\"\n")

                            /** End of YAML Leaf Node Substitution */

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
            .append("            \"module\": \"module.aws_route_table_association[\\\"tofu\\\"]\",\n")
            .append("            \"mode\": \"managed\",\n")
            .append("            \"type\": \"aws_route_table_association\",\n")
            .append("            \"name\": \"product\",\n")
            .append("            \"provider\": \"provider[\\\"registry.terraform.io/hashicorp/aws\\\"]\",\n")
            .append("            \"instances\": [\n");
                for (int index = 0; index < resourceConfigs.size(); index++) {
                    Map<String, Object> resourceConfig = resourceConfigs.get(index);
                    try {

                        /** Generate index_key value */

                        String routeTableAssociationId = (String) resourceConfig.get("route_table_association_id");
                        if (!routeTableAssociationId.contains("create-only-")) {
                            if (routeTableAssociationId.startsWith("arn:")) {
                                String resource = routeTableAssociationId.split(":", 6)[5];
                                String[] tokens = resource.split("[/:]");
                                routeTableAssociationId = String.join("-", tokens);
                            }
                        }

                        definedPythonFile
                            .append("                {\n")
                            .append("                    \"index_key\": \"" + routeTableAssociationId + "\",\n")
                            .append("                    \"attributes\": {\n");
                            if (!((String) resourceConfig.get("route_table_association_id")).contains("create-only")) {
                                definedPythonFile
                                    .append("                        \"id\": \"" + resourceConfig.get("route_table_association_id") + "\"\n");
                            };
                        definedPythonFile
                            .append("                    }\n")
                            .append("                }");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_state_file_route_table_association_id");
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
