package click.opentofu.sprout.service.implementations;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import click.opentofu.sprout.dto.sts.TemporaryCredential;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.GeneralUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteStsUtils implements AsyncServiceSingle {

    private final TaskExecutor taskExecutor;
    private final GeneralUtils generalUtils;

    @Override
    @Async("taskExecutor")
    public <T> CompletableFuture<Object> mainWorkerAsync(T dto, SseEmitter unusedEmitter) {
        return AsyncServiceSingle.super.mainWorkerAsync(dto, unusedEmitter);
    }

    @Override
    public <T> CompletableFuture<Object> workerSingleSupplyAsync(T dto, SseEmitter unusedEmitter) {
        TemporaryCredential temporaryCredential = generalUtils.castTemporaryCredential(dto);
        String authEmailId = temporaryCredential.getAuthEmailId();
        temporaryCredential.setIsNextCallable(false);
        return asyncWorkerSupply(
            () -> {
                try {
                    log.warn("------------------------------------------");
                    log.warn("deleteStsUtils-started for delete sts utils ! Requester: " + authEmailId);
                    log.warn("------------------------------------------");
                    Path filePath = Paths.get(ROOT_PATH, "sts", authEmailId);
                    if (
                        Files.exists(filePath) &&
                        Files.isDirectory(filePath)
                    ) {
                        Files.walk(filePath)
                            .filter((path) -> { return !path.equals(filePath); })
                            .sorted(Comparator.reverseOrder())
                            .map((path) -> { return path.toFile(); })
                            .forEach((file) -> { file.delete(); });
                    }
                } catch (Exception error) {
                    throw new RuntimeException("Failed to execute delete root path due to an unexpected exception \nFind the cause of the problem in 'service/implementations/DeleteStsUtils.java'");
                }
                temporaryCredential.setIsNextCallable(true);
                return "success";
            },
            taskExecutor
        );
    }
}
