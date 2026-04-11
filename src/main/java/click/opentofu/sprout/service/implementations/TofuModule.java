package click.opentofu.sprout.service.implementations;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.opentofu.sprout.dto.request.ResourceDto;
import click.opentofu.sprout.handler.tofu.module.interfaces.ModuleHandler;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.GeneralUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TofuModule implements AsyncServiceSingle {

    private final TaskExecutor taskExecutor;
    private final GeneralUtils generalUtils;
    private final Map<String, ModuleHandler> moduleHandlers;
    
    @Override
    @Async("taskExecutor")
    public <T> CompletableFuture<Object> mainWorkerAsync(T dto, SseEmitter unusedEmitter) {
        return AsyncServiceSingle.super.mainWorkerAsync(dto, unusedEmitter);
    }

    @Override
    public <T> CompletableFuture<Object> workerSingleSupplyAsync(T dto, SseEmitter unusedEmitter) {
        ResourceDto resourceDto = generalUtils.castAwsCloudRequest(dto);

        Map<String, Object> token = resourceDto.getToken();
        String authEmailId = (String) token.get("auth_email_id");
        String operation = resourceDto.getOperation();

        resourceDto.setIsNextCallable(false);

        return asyncWorkerSupply(
            () -> {
                try {
                    
                    log.info("-------------------------------------------------------------------------------------------------------------------------");
                    log.info("AsyncServiceSingle-started for the tofu module to generate the 'module main {}' & config.yaml & terraform.tfstate files.");
                    log.info("-------------------------------------------------------------------------------------------------------------------------");

                    String uuid = ((String) token.get("account_id")).split(",")[1];
                    String regionCode = resourceDto.getRegionCode();
                    String awsAccessKey = (String) token.get("aws_access_key");
                    String awsSecretAccessKey = (String) token.get("aws_secret_access_key");
                    String awsSessionToken = (String) token.get("aws_session_token");

                    String moduleName = resourceDto.getModuleName();

                    String beanName = "tofu_module_" + moduleName.substring("aws_".length());

                    Path configFilePath = Paths.get(ROOT_PATH, authEmailId, uuid, regionCode, "tofu_module", moduleName, "config.yaml");
                    Path mainFilePath = Paths.get(ROOT_PATH, authEmailId, uuid, regionCode, "tofu_module", moduleName, "main.tf");
                    Path stateFilePath = Paths.get(ROOT_PATH, authEmailId, uuid, regionCode, "tofu_module", moduleName, "terraform.tfstate");

                    boolean configFileExists = Files.exists(configFilePath);
                    boolean mainFileExists = Files.exists(mainFilePath);
                    boolean stateFileExists = Files.exists(stateFilePath);

                    ModuleHandler moduleHandler = moduleHandlers.get(beanName);

                    List<Map<String, Object>> draftVersion = resourceDto.getDraftVersion();

                    if (draftVersion != null) {
                        String mainFileMergeString = moduleHandler.buildTofuModuleMainFile(regionCode, awsAccessKey, awsSecretAccessKey, awsSessionToken);
                        String configFileMergeString = moduleHandler.buildTofuModuleConfigFile(draftVersion, operation);
                        String stateFileMergeString = moduleHandler.buildTofuModuleStateFile(draftVersion);

                        moduleHandler.mainWorkerModule(mainFileExists, mainFilePath, mainFileMergeString);
                        log.info("--------------------------------------------------------------------------------");
                        log.info("Completed Create tofu module main.tf file. Requester: " + authEmailId);
                        log.info("--------------------------------------------------------------------------------");

                        moduleHandler.mainWorkerModule(configFileExists, configFilePath, configFileMergeString);
                        log.info("--------------------------------------------------------------------------------");
                        log.info("Completed Create tofu module config.yaml file. Requester: " + authEmailId);
                        log.info("--------------------------------------------------------------------------------");

                        moduleHandler.mainWorkerModule(stateFileExists, stateFilePath, stateFileMergeString);
                        log.info("--------------------------------------------------------------------------------");
                        log.info("Completed Create tofu module tofu.tfstate file. Requester: " + authEmailId);
                        log.info("--------------------------------------------------------------------------------");

                        if (!TEXTAREA_PARAMS_BY_MODULE.getOrDefault(moduleName, List.of()).isEmpty()) {

                            for (Map<String, Object> obj : draftVersion) {

                                String moduleId;

                                switch (moduleName) {

                                    case "aws_vpc":
                                        moduleId = (String) obj.get("vpc_id");
                                        break;

                                    case "aws_vpc_ipam_pool":
                                        moduleId = (String) obj.get("vpc_ipam_pool_id");
                                        break;

                                    case "aws_instance":
                                        moduleId = (String) obj.get("instance_id");
                                        break;

                                    case "aws_subnet":
                                        moduleId = (String) obj.get("subnet_id");
                                        break;

                                    case "aws_security_group":
                                        moduleId = (String) obj.get("security_group_id");
                                        break;

                                    case "aws_internet_gateway":
                                        moduleId = (String) obj.get("internet_gateway_id");
                                        break;

                                    case "aws_route_table":
                                        moduleId = (String) obj.get("route_table_id");
                                        break;

                                    case "aws_route_table_association":
                                        moduleId = (String) obj.get("route_table_association_id");
                                        break;

                                    default:
                                        throw new RuntimeException("tofu_module_service_layer_async_worker_supply_module_id_define_switch_default");
                                }

                                if (moduleId == null) { continue; };

                                /** Normalize Id for filename */

                                if (!moduleId.contains("create-only-")) {
                                    if (moduleId.startsWith("arn:")) {
                                        String resource = moduleId.split(":", 6)[5];
                                        String[] tokens = resource.split("[/:]");
                                        moduleId = String.join("-", tokens);
                                    }
                                }

                                for (String key : TEXTAREA_PARAMS_BY_MODULE.getOrDefault(moduleName, List.of())) {
                                    Object val = obj.get(key);

                                    if (val != null && (val instanceof String || val instanceof List || val instanceof Map)) {
                                        Path textareaFilePath;

                                        /** File extension varies by textarea parameter */

                                        if ("aws_ecs_task_definition".equals(moduleName) && "container_definitions".equals(key)) {
                                            textareaFilePath = Paths.get(ROOT_PATH, authEmailId, uuid, regionCode, "tofu_module", moduleName, moduleId + "_" + key + ".json");
                                        } else if ("aws_instance".equals(moduleName) && "user_data".equals(key)) {
                                            textareaFilePath = Paths.get(ROOT_PATH, authEmailId, uuid, regionCode, "tofu_module", moduleName, moduleId + "_" + key + ".sh");
                                        } else {
                                            textareaFilePath = Paths.get(ROOT_PATH, authEmailId, uuid, regionCode, "tofu_module", moduleName, moduleId + "_" + key + ".default");
                                        }

                                        boolean textareaFileExists = Files.exists(textareaFilePath);
                                        String unescaped = null;

                                        if (val instanceof String) { unescaped = StringEscapeUtils.unescapeJava((String) val); }

                                        else if (val instanceof List || val instanceof Map) {
                                            ObjectMapper mapper = new ObjectMapper();
                                            mapper.enable(SerializationFeature.INDENT_OUTPUT);

                                            DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
                                            DefaultIndenter indenter = new DefaultIndenter("    ", "\n");
                                            prettyPrinter.indentObjectsWith(indenter);
                                            prettyPrinter.indentArraysWith(indenter);

                                            unescaped = StringEscapeUtils.unescapeJava(mapper.writer(prettyPrinter).writeValueAsString(val));
                                        }

                                        String textareaFileMergeString = moduleHandler.buildTofuTextareaFile(unescaped);

                                        moduleHandler.mainWorkerModule(textareaFileExists, textareaFilePath, textareaFileMergeString);
                                        log.info("----------------------------------------------------------------------------------------------");
                                        log.info("Completed Create tofu textarea " + moduleId + "_" + key + " file. Requester: " + authEmailId);
                                        log.info("----------------------------------------------------------------------------------------------");
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception error) {
                    throw new RuntimeException(error);
                }

                resourceDto.setIsNextCallable(true);
                return new Object();
            },
            taskExecutor
        );  
    }
}
