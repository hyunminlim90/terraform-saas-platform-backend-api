package click.opentofu.sprout.handler.tofu.module.implementations;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.module.interfaces.ModuleHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("tofu_module_internet_gateway")
public class TofuModuleInternetGateway implements ModuleHandler {

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
            .append("module \"aws_internet_gateway\" {\n")
            .append("    source = \"../../tofu_resource/aws_internet_gateway\"\n\n")
            .append("    for_each = local.config.awscloud\n\n")
            .append("    aws_internet_gateway = {\n")
            .append("        for index, aws_internet_gateway in try(each.value.aws_internet_gateway, []) :\n")
            .append("            \"${regex(\"^[a-zA-Z0-9][a-zA-Z0-9._/: -]{1,254}$\", aws_internet_gateway.name)}\" => {\n")

            /** Start of Substitution */

            .append("                vpc_id = aws_internet_gateway.vpc_id\n")
            .append("                tags   = aws_internet_gateway.tags\n")

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
            .append("# https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/internet_gateway#argument-reference\n\n")
            .append("context:\n\n")
            .append("    # Match the 'Uuid of Arn' parameter.\n")
            .append("    name:\n");

        for (Map<String, Object> resourceConfig : resourceConfigs) {
            try {
                String internetGatewayId = (String) resourceConfig.get("internet_gateway_id");
                if (!internetGatewayId.contains("create-only-")) {
                    if (internetGatewayId.startsWith("arn:")) {
                        String resource = internetGatewayId.split(":", 6)[5];
                        String[] tokens = resource.split("[/:]");
                        internetGatewayId = String.join("-", tokens);
                    }
                }
                definedPythonFile
                    .append("        - \"" + internetGatewayId + "\"\n");
            } catch (Exception error) {
                throw new RuntimeException("build_tofu_module_config_file_internet_gateway_id");
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
                .append("        \"aws_internet_gateway\": []\n\n");
        } else {
            definedPythonFile
                .append("        \"aws_internet_gateway\":\n\n");

                    for (int index = 0; index < resourceConfigs.size(); index++) {
                        definedPythonFile
                            .append("            - {\n")
                            .append("                name: \"${name[" + index + "]}\",\n")

                            /** Start of YAML Leaf Node Substitution */

                            .append("                vpc_id: \"${vpc_id[" + index + "]}\",\n")

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
            .append("            \"module\": \"module.aws_internet_gateway[\\\"tofu\\\"]\",\n")
            .append("            \"mode\": \"managed\",\n")
            .append("            \"type\": \"aws_internet_gateway\",\n")
            .append("            \"name\": \"product\",\n")
            .append("            \"provider\": \"provider[\\\"registry.terraform.io/hashicorp/aws\\\"]\",\n")
            .append("            \"instances\": [\n");
                for (int index = 0; index < resourceConfigs.size(); index++) {
                    Map<String, Object> resourceConfig = resourceConfigs.get(index);
                    try {

                        /** Generate index_key value */

                        String internetGatewayId = (String) resourceConfig.get("internet_gateway_id");
                        if (!internetGatewayId.contains("create-only-")) {
                            if (internetGatewayId.startsWith("arn:")) {
                                String resource = internetGatewayId.split(":", 6)[5];
                                String[] tokens = resource.split("[/:]");
                                internetGatewayId = String.join("-", tokens);
                            }
                        }

                        definedPythonFile
                            .append("                {\n")
                            .append("                    \"index_key\": \"" + internetGatewayId + "\",\n")
                            .append("                    \"attributes\": {\n");
                            if (!((String) resourceConfig.get("internet_gateway_id")).contains("create-only")) {
                                definedPythonFile
                                    .append("                        \"id\": \"" + resourceConfig.get("internet_gateway_id") + "\"\n");
                            };
                        definedPythonFile
                            .append("                    }\n")
                            .append("                }");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_state_file_internet_gateway_id");
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
