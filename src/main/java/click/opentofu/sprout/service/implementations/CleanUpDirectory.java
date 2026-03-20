package click.opentofu.sprout.service.implementations;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import click.opentofu.sprout.dto.ResourceDto;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.GeneralUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CleanUpDirectory implements AsyncServiceSingle {
    
    private final TaskExecutor taskExecutor;
    private final GeneralUtils generalUtils;

    @Override
    @Async("taskExecutor")
    public <T> CompletableFuture<Object> mainWorkerAsync(T dto, SseEmitter unusedEmitter) {
        return AsyncServiceSingle.super.mainWorkerAsync(dto, unusedEmitter);
    }

    @Override
    public <T> CompletableFuture<Object> workerSingleSupplyAsync(T dto, SseEmitter unusedEmitter) {
        ResourceDto resourceDto = generalUtils.castAwsCloudRequest(dto);
        Map<String, Object> token = resourceDto.getToken();
        
        resourceDto.setIsNextCallable(false);

        return asyncWorkerSupply(
            () -> {
                try {
                    log.warn("---------------------------------------------------------------");
                    log.warn("AsyncServiceSingle-started for the directory deletion process.");
                    log.warn("---------------------------------------------------------------");

                    final Path[] deleteDirectoryPath = new Path[1];

                    String uuid = ((String) token.get("account_id")).split(",")[1];
                    String authEmailId = (String) token.get("auth_email_id");

                    deleteDirectoryPath[0] = Paths.get(ROOT_PATH, authEmailId, uuid);

                    if (deleteDirectoryPath[0] != null) {
                        generalUtils.deleteDirectoryRecursively(deleteDirectoryPath[0]);
                        log.info("Successfully deleted directory: " + deleteDirectoryPath[0].toString());
                    }

                    log.warn("-----------------------------------------------------------------------------");
                    log.warn("Completed deletion process for the directory. Requester: " + authEmailId);
                    log.warn("-----------------------------------------------------------------------------");

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
