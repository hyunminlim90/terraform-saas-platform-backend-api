package click.opentofu.sprout.service.implementations;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import click.opentofu.sprout.dto.interfaces.ModuleDto;
import click.opentofu.sprout.dto.request.ResourceDto;
import click.opentofu.sprout.dto.response.VpcDto;
import click.opentofu.sprout.dto.response.VpcIpamPoolDto;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.GeneralUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JsonNodeLoadResource implements AsyncServiceSingle {

    private final TaskExecutor taskExecutor;
    private final GeneralUtils generalUtils;

    private final ObjectMapper mapper;
    
    @Override
    @Async("taskExecutor")
    public <T> CompletableFuture<Object> mainWorkerAsync(T dto, SseEmitter unusedEmitter) {
        return AsyncServiceSingle.super.mainWorkerAsync(dto, unusedEmitter);
    }

    @Override
    public <T> CompletableFuture<Object> workerSingleSupplyAsync(T dto, SseEmitter unusedEmitter) {
        ResourceDto resourceDto = generalUtils.castAwsCloudRequest(dto);
        Map<String, Object> token = resourceDto.getToken();

        final Path[] deleteBoto3DirectoryPath = new Path[1];

        List<ModuleDto> resources = new ArrayList<>();
        
        resourceDto.setIsNextCallable(false);

        return asyncWorkerSupply(
            () -> {
                try {
                    log.warn("---------------------------------------------------------------------------------------------------------------------------------------");
                    log.warn("AsyncServiceSingle-started for executing a Python script using Boto3 to generate a JSON file, then reading the result as a JSON node.");
                    log.warn("---------------------------------------------------------------------------------------------------------------------------------------");

                    String uuid = ((String) token.get("account_id")).split(",")[1];
                    String authEmailId = (String) token.get("auth_email_id");
                    String region = resourceDto.getRegionCode();

                    String moduleName = resourceDto.getModuleName();

                    Path filePath = Paths.get(ROOT_PATH, authEmailId, uuid, region, "boto3", moduleName + ".json");
                    deleteBoto3DirectoryPath[0] = Paths.get(ROOT_PATH, authEmailId, uuid, region, "boto3");
                    File jsonFile = new File(filePath.toString());
                    JsonNode attributesNode = mapper.readTree(jsonFile);
                    String atPath = "/outputs/" + moduleName + "/value";

                    Set<Map.Entry<String, JsonNode>> entries = attributesNode.at(atPath).properties();

                    log.warn("----------------------------------------------------------");
                    log.warn("Completed 'Read .json' And Save ! Requester: " + authEmailId);
                    log.warn("----------------------------------------------------------");

                    /** Json 필드와 Dto 필드 매핑 실패 시 해당 필드는 무시하고 진행 */
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    for (Map.Entry<String, JsonNode> entry : entries) {
                        
                        ModuleDto resource;

                        switch (moduleName) {
                            // entry.getKey();

                            case "aws_vpc":
                                resource = mapper.convertValue(entry.getValue(), VpcDto.class);
                                break;

                            case "aws_vpc_ipam_pool":
                                resource = mapper.convertValue(entry.getValue(), VpcIpamPoolDto.class);
                                break;

                            default:
                                throw new RuntimeException("json_node_load_resource_service_layer_async_worker_supply_switch_default");
                        }
                        resources.add(resource);
                    }
                } catch (Exception error) {
                    throw new RuntimeException(error);
                }
                return resources;
            },
            taskExecutor
        )
        .whenComplete(
            (result, throwable) -> {
                CompletableFuture.runAsync(
                    () -> {
                        try {
                            if (deleteBoto3DirectoryPath[0] != null) {
                                generalUtils.deleteDirectoryRecursively(deleteBoto3DirectoryPath[0]);
                                log.info("Successfully deleted directory: " + deleteBoto3DirectoryPath[0].toString());
                            }
                        } catch (Exception error) {
                            throw new RuntimeException(error);
                        }
                    }
                );
            }
        );
    }
}
