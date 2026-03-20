package click.opentofu.sprout.service.implementations;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import click.opentofu.sprout.dto.ResourceDto;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.GeneralUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DuplicateDraftVersions implements AsyncServiceSingle {

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
        String accountId = ((String) token.get("account_id")).split(",")[0];
        String region = resourceDto.getRegionCode();
        String authUserIndex = resourceDto.getAuthUserIndex();
        String authEmailId = (String) token.get("auth_email_id");
        List<List<Map<String, Object>>> duplicateDraftVersion = resourceDto.getDuplicateDraftVersion();

        resourceDto.setIsNextCallable(false);

        return asyncWorkerSupply(
            () -> {
                log.info("--------------------------------------------------------------------------------------------------------------------------------------------");
                log.info("AsyncServiceSingle-started for duplicating draft version received from the frontend to the database using an duplicate entity builder class.");
                log.info("--------------------------------------------------------------------------------------------------------------------------------------------");

                String beanName = "duplicate_entity_builder_sprout";
                EntityHandler entityHandler = entityHandlers.get(beanName);

                for (List<Map<String, Object>> innerList : duplicateDraftVersion) {
                    for (Map<String, Object> draftVersion : innerList) {
                        JsonNode parameters = mapper.valueToTree(draftVersion);
                        entityHandler.duplicateBuildEntityAndSave(parameters, authUserIndex, accountId, region);
                    }
                }

                log.info("--------------------------------------------------------");
                log.info("Completed duplicate entity save. Requester : " + authEmailId);
                log.info("--------------------------------------------------------");                

                return new Object();
            },
            taskExecutor
        );  
    }
}
