package click.opentofu.sprout.service.implementations;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.GeneralUtils;
import click.opentofu.sprout.util.EmitterUtils;
import click.opentofu.sprout.dto.ResourceDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssueUniqueId implements AsyncServiceSingle {
    
    private final TaskExecutor taskExecutor;
    private final GeneralUtils generalUtils;
    private final EmitterUtils emitterUtils;

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

        resourceDto.setIsNextCallable(false);

        return asyncWorkerSupply(
            () -> {
                log.info("----------------------------------------------------------------------------------------------");
                log.info("AsyncServiceSingle-started for Creating a new SSE emitter and registering it with a unique ID.");
                log.info("----------------------------------------------------------------------------------------------");

                SseEmitter sseEmitter = emitterUtils.createEmitter();
                String uniqueId = emitterUtils.registerNewEmitter(sseEmitter);
                
                log.info("----------------------------------------------------------------------------------------");
                log.info("Completed SSE emitter and registering it with a unique ID. Requester : " + authEmailId);
                log.info("----------------------------------------------------------------------------------------");                

                return uniqueId;
            },
            taskExecutor
        );  
    }
}
