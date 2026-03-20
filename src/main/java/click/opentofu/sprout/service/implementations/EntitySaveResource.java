package click.opentofu.sprout.service.implementations;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.GeneralUtils;
import click.opentofu.sprout.dto.ResourceDto;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntitySaveResource implements AsyncServiceSingle {
    
    private final TaskExecutor taskExecutor;
    private final GeneralUtils generalUtils;

    private final Map<String, EntityHandler> entityHandlers;

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
        Map<String, Object> resource = resourceDto.getResource();
        String authUserIndex = resourceDto.getAuthUserIndex();
        String resourceSaveName = resourceDto.getResourceSaveName();
        String authEmailId = (String) token.get("auth_email_id");
        
        resourceDto.setIsNextCallable(false);

        return asyncWorkerSupply(
            () -> {
                log.info("-----------------------------------------------------------------------------------------------------------------------------");
                log.info("AsyncServiceSingle-started for saving resource data received from the frontend to the database using an entity builder class.");
                log.info("-----------------------------------------------------------------------------------------------------------------------------");

                String beanName = "entity_builder_sprout";
                EntityHandler entityHandler = entityHandlers.get(beanName);

                Set<Map.Entry<String, JsonNode>> entries = mapper.valueToTree(resource).properties();
                for (Map.Entry<String, JsonNode> entry : entries) {
                    JsonNode value = entry.getValue();
                    if (value.isArray()) {
                        for (JsonNode parameters : value) {
                            entityHandler.buildEntityAndSave(parameters, resourceSaveName, authUserIndex);
                        }
                    } else {
                        throw new RuntimeException("entity_save_resource_value_is_array");
                    }
                }

                log.info("---------------------------------------------------");
                log.info("Completed entity save. Requester : " + authEmailId);
                log.info("---------------------------------------------------");                

                return new Object();
            },
            taskExecutor
        );  
    }
}
