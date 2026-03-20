package click.opentofu.sprout.handler.tofu.module.implementations;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.handler.tofu.module.interfaces.ModuleHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("tofu_module")
public class TofuModule implements ModuleHandler {

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
            .append("module \"aws_sprout\" {\n")
            .append("    source = \"../../tofu_resource/aws_sprout\"\n\n")
            .append("    for_each = local.config.awscloud\n\n")
            .append("    aws_sprout = {\n")
            .append("        for index, aws_sprout in try(each.value.aws_sprout, []) :\n")
            .append("            \"${regex(\"^[a-zA-Z0-9][a-zA-Z0-9._/: -]{1,254}$\", aws_sprout.name)}\" => {\n")

            /** Start of Substitution */

            .append("                container_definitions    = aws_sprout.container_definitions\n")
            .append("                cpu                      = aws_sprout.cpu\n")
            .append("                enable_fault_injection   = aws_sprout.enable_fault_injection\n")
            .append("                size_in_gib              = aws_sprout.size_in_gib\n")
            .append("                execution_role_arn       = aws_sprout.execution_role_arn\n")
            .append("                family                   = aws_sprout.family\n")
            .append("                ipc_mode                 = aws_sprout.ipc_mode\n")
            .append("                memory                   = aws_sprout.memory\n")
            .append("                network_mode             = aws_sprout.network_mode\n")
            .append("                pid_mode                 = aws_sprout.pid_mode\n")
            .append("                expression               = aws_sprout.expression\n")
            .append("                constraints_type         = aws_sprout.constraints_type\n")
            .append("                container_name           = aws_sprout.container_name\n")
            .append("                proxy_type               = aws_sprout.proxy_type\n")
            .append("                AppPorts                 = aws_sprout.AppPorts\n")
            .append("                EgressIgnoredIPs         = aws_sprout.EgressIgnoredIPs\n")
            .append("                IgnoredUID               = aws_sprout.IgnoredUID\n")
            .append("                ProxyEgressPort          = aws_sprout.ProxyEgressPort\n")
            .append("                ProxyIngressPort         = aws_sprout.ProxyIngressPort\n")
            .append("                region                   = aws_sprout.region\n")
            .append("                requires_compatibilities = aws_sprout.requires_compatibilities\n")
            .append("                operating_system_family  = aws_sprout.operating_system_family\n")
            .append("                cpu_architecture         = aws_sprout.cpu_architecture\n")
            .append("                skip_destroy             = aws_sprout.skip_destroy\n")
            .append("                task_role_arn            = aws_sprout.task_role_arn\n")
            .append("                track_latest             = aws_sprout.track_latest\n")
            .append("                volume_name              = aws_sprout.volume_name\n")
            .append("                host_path                = aws_sprout.host_path\n")
            .append("                configure_at_launch      = aws_sprout.configure_at_launch\n")
            .append("                autoprovision            = aws_sprout.autoprovision\n")
            .append("                driver                   = aws_sprout.driver\n")
            .append("                scope                    = aws_sprout.scope\n")
            .append("                labels                   = aws_sprout.labels\n")
            .append("                driver_opts              = aws_sprout.driver_opts\n")
            .append("                efs_file_system_id       = aws_sprout.efs_file_system_id\n")
            .append("                efs_root_directory       = aws_sprout.efs_root_directory\n")
            .append("                transit_encryption       = aws_sprout.transit_encryption\n")
            .append("                transit_encryption_port  = aws_sprout.transit_encryption_port\n")
            .append("                access_point_id          = aws_sprout.access_point_id\n")
            .append("                iam                      = aws_sprout.iam\n")
            .append("                fsx_file_system_id       = aws_sprout.fsx_file_system_id\n")
            .append("                fsx_root_directory       = aws_sprout.fsx_root_directory\n")
            .append("                credentials_parameter    = aws_sprout.credentials_parameter\n")
            .append("                domain                   = aws_sprout.domain\n")
            .append("                tags                     = aws_sprout.tags\n")

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
            .append("# https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/sprout#argument-reference\n\n")
            .append("context:\n\n")
            .append("    # Match the 'Uuid of Arn' parameter.\n")
            .append("    name:\n");

        for (Map<String, Object> resourceConfig : resourceConfigs) {
            try {
                String taskId = (String) resourceConfig.get("task_id");
                if (!taskId.contains("create-only-")) {
                    if (taskId.startsWith("arn:")) {
                        String resource = taskId.split(":", 6)[5];
                        String[] tokens = resource.split("[/:]");
                        taskId = String.join("-", tokens);
                    }
                }
                definedPythonFile
                    .append("        - \"" + taskId + "\"\n");
            } catch (Exception error) {
                throw new RuntimeException("build_tofu_module_config_file_id");
            }
        }

        /** Start of YAML Field Mapping */




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
                .append("        \"aws_sprout\": []\n\n");
        } else {
            definedPythonFile
                .append("        \"aws_sprout\":\n\n");

                    for (int index = 0; index < resourceConfigs.size(); index++) {
                        definedPythonFile
                            .append("            - {\n")
                            .append("                name: \"${name[" + index + "]}\",\n")

                            /** Start of YAML Leaf Node Substitution */

                            .append("                container_definitions: \"${container_definitions[" + index + "]}\",\n")
                            .append("                cpu: \"${cpu[" + index + "]}\",\n")
                            .append("                enable_fault_injection: \"${enable_fault_injection[" + index + "]}\",\n")
                            .append("                size_in_gib: \"${size_in_gib[" + index + "]}\",\n")
                            .append("                execution_role_arn: \"${execution_role_arn[" + index + "]}\",\n")
                            .append("                family: \"${family[" + index + "]}\",\n")
                            .append("                ipc_mode: \"${ipc_mode[" + index + "]}\",\n")
                            .append("                memory: \"${memory[" + index + "]}\",\n")
                            .append("                network_mode: \"${network_mode[" + index + "]}\",\n")
                            .append("                pid_mode: \"${pid_mode[" + index + "]}\",\n")
                            .append("                expression: \"${expression[" + index + "]}\",\n")
                            .append("                constraints_type: \"${constraints_type[" + index + "]}\",\n")
                            .append("                container_name: \"${container_name[" + index + "]}\",\n")
                            .append("                proxy_type: \"${proxy_type[" + index + "]}\",\n")
                            .append("                AppPorts: \"${AppPorts[" + index + "]}\",\n")
                            .append("                EgressIgnoredIPs: \"${EgressIgnoredIPs[" + index + "]}\",\n")
                            .append("                IgnoredUID: \"${IgnoredUID[" + index + "]}\",\n")
                            .append("                ProxyEgressPort: \"${ProxyEgressPort[" + index + "]}\",\n")
                            .append("                ProxyIngressPort: \"${ProxyIngressPort[" + index + "]}\",\n")
                            .append("                region: \"${region[" + index + "]}\",\n")
                            .append("                requires_compatibilities: \"${requires_compatibilities[" + index + "]}\",\n")
                            .append("                operating_system_family: \"${operating_system_family[" + index + "]}\",\n")
                            .append("                cpu_architecture: \"${cpu_architecture[" + index + "]}\",\n")
                            .append("                skip_destroy: \"${skip_destroy[" + index + "]}\",\n")
                            .append("                task_role_arn: \"${task_role_arn[" + index + "]}\",\n")
                            .append("                track_latest: \"${track_latest[" + index + "]}\",\n")
                            .append("                volume_name: \"${volume_name[" + index + "]}\",\n")
                            .append("                host_path: \"${host_path[" + index + "]}\",\n")
                            .append("                configure_at_launch: \"${configure_at_launch[" + index + "]}\",\n")
                            .append("                autoprovision: \"${autoprovision[" + index + "]}\",\n")
                            .append("                driver: \"${driver[" + index + "]}\",\n")
                            .append("                scope: \"${scope[" + index + "]}\",\n")
                            .append("                labels: \"${labels[" + index + "]}\",\n")
                            .append("                driver_opts: \"${driver_opts[" + index + "]}\",\n")
                            .append("                efs_file_system_id: \"${efs_file_system_id[" + index + "]}\",\n")
                            .append("                efs_root_directory: \"${efs_root_directory[" + index + "]}\",\n")
                            .append("                transit_encryption: \"${transit_encryption[" + index + "]}\",\n")
                            .append("                transit_encryption_port: \"${transit_encryption_port[" + index + "]}\",\n")
                            .append("                access_point_id: \"${access_point_id[" + index + "]}\",\n")
                            .append("                iam: \"${iam[" + index + "]}\",\n")
                            .append("                fsx_file_system_id: \"${fsx_file_system_id[" + index + "]}\",\n")
                            .append("                fsx_root_directory: \"${fsx_root_directory[" + index + "]}\",\n")
                            .append("                credentials_parameter: \"${credentials_parameter[" + index + "]}\",\n")
                            .append("                domain: \"${domain[" + index + "]}\",\n")

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
            .append("            \"module\": \"module.aws_sprout[\\\"tofu\\\"]\",\n")
            .append("            \"mode\": \"managed\",\n")
            .append("            \"type\": \"aws_sprout\",\n")
            .append("            \"name\": \"product\",\n")
            .append("            \"provider\": \"provider[\\\"registry.terraform.io/hashicorp/aws\\\"]\",\n")
            .append("            \"instances\": [\n");
                for (int index = 0; index < resourceConfigs.size(); index++) {
                    Map<String, Object> resourceConfig = resourceConfigs.get(index);
                    try {

                        /** Generate index_key value */

                        String taskId = (String) resourceConfig.get("task_id");
                        if (!taskId.contains("create-only-")) {
                            if (taskId.startsWith("arn:")) {
                                String resource = taskId.split(":", 6)[5];
                                String[] tokens = resource.split("[/:]");
                                taskId = String.join("-", tokens);
                            }
                        }

                        definedPythonFile
                            .append("                {\n")
                            .append("                    \"index_key\": \"" + taskId + "\",\n")
                            .append("                    \"attributes\": {\n");
                            if (!((String) resourceConfig.get("task_id")).contains("create-only")) {
                                definedPythonFile
                                    .append("                        \"arn\": \"" + resourceConfig.get("task_arn") + "\",\n")
                                    .append("                        \"id\": \"" + ((String) resourceConfig.get("task_id")).split("-")[0] + "\"\n");
                            };
                        definedPythonFile
                            .append("                    }\n")
                            .append("                }");
                    } catch (Exception error) {
                        throw new RuntimeException("build_tofu_module_state_file_task_id");
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
