package click.opentofu.sprout.service.implementations;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RSemaphore;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.GeneralUtils;
import click.opentofu.sprout.util.ProcessUtils;
import click.opentofu.sprout.dto.request.ResourceDto;
import click.opentofu.sprout.redis.SemaphoreProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExecTofuApply implements AsyncServiceSingle {

    private final TaskExecutor taskExecutor;
    private final GeneralUtils generalUtils;
    private final ProcessUtils processUtils;

    private final SemaphoreProvider semaphoreProvider;
    
    @Override
    @Async("taskExecutor")
    public <T> CompletableFuture<Object> mainWorkerAsync(T dto, SseEmitter emitter) {
        return AsyncServiceSingle.super.mainWorkerAsync(dto, emitter);
    }

    @Override
    public <T> CompletableFuture<Object> workerSingleSupplyAsync(T dto, SseEmitter emitter) {
        ResourceDto resourceDto = generalUtils.castAwsCloudRequest(dto);

        Map<String, Object> token = resourceDto.getToken();
        String authEmailId = (String) token.get("auth_email_id");

        final Path[] deleteBoto3DirectoryPath = new Path[1];

        String moduleName = resourceDto.getModuleName();

        resourceDto.setIsNextCallable(false);

        return asyncWorkerSupply(
            () -> {

                RSemaphore semaphore = semaphoreProvider.getApplySemaphore();
                boolean acquired = false;

                try {

                    acquired = semaphore.tryAcquire(60, TimeUnit.SECONDS);

                    if (!acquired) {
                        String msg = "❌ tofu apply queue is full. Please try again later.";
                        emitter.send(SseEmitter.event().data(msg));
                        throw new RuntimeException("exec_tofu_apply_semaphore_queue_is_full");
                    }

                    log.info("-----------------------------------------------------------------");
                    log.info("AsyncServiceSingle-started for Tofu init, apply Command execution.");
                    log.info("-----------------------------------------------------------------");

                    String uuid = ((String) token.get("account_id")).split(",")[1];
                    String regionCode = resourceDto.getRegionCode();
                    Path workingDirPath = Paths.get(ROOT_PATH, authEmailId, uuid, regionCode, "tofu_module", moduleName);

                    deleteBoto3DirectoryPath[0] = Paths.get(ROOT_PATH, authEmailId);

                    processUtils.execTofuCommand(
                        Arrays.asList("tofu", "init"),
                        workingDirPath,
                        emitter
                    );
                    processUtils.execTofuCommand(
                        Arrays.asList("tofu", "apply", "-auto-approve"),
                        workingDirPath,
                        emitter
                    );
                    emitter.send(SseEmitter.event().data("🎉 All tofu commands executed successfully."));
                    emitter.complete();

                    log.info("----------------------------------------------------------------------------------------");
                    log.info("Completed Tofu init, apply Command execution. Requester : " + authEmailId);
                    log.info("----------------------------------------------------------------------------------------");

                } catch (Exception error) {
                    emitter.completeWithError(error);
                    throw new RuntimeException(error);
                } finally {
                    if (acquired) {
                        semaphore.release();
                    }
                }

                return new Object();
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
