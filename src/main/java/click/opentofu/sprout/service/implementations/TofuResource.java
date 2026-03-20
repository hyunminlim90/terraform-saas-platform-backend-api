package click.opentofu.sprout.service.implementations;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import click.opentofu.sprout.handler.tofu.resource.interfaces.ResourceHandler;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.GeneralUtils;
import click.opentofu.sprout.dto.ResourceDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TofuResource implements AsyncServiceSingle {

    private final TaskExecutor taskExecutor;
    private final GeneralUtils generalUtils;
    private final Map<String, ResourceHandler> resourceHandlers;

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
                try {

                log.info("-------------------------------------------------------------------------------------------");
                log.info("AsyncServiceSingle-started for the tofu resource to generate the 'resource main {}' blocks.");
                log.info("-------------------------------------------------------------------------------------------");

                String accountId = ((String) token.get("account_id")).split(",")[0];
                String uuid = ((String) token.get("account_id")).split(",")[1];
                String regionCode = resourceDto.getRegionCode();
                
                String beanName = "tofu_resource";
                Path filePath = Paths.get(ROOT_PATH, authEmailId, uuid, regionCode, "tofu_resource", "aws_sprout", "main.tf");
                boolean fileExists = Files.exists(filePath);

                ResourceHandler resourceHandler = resourceHandlers.get(beanName);
                String mergeString = resourceHandler.buildTofuResourceMainFile(regionCode, accountId);
                resourceHandler.mainWorkerResource(fileExists, filePath, mergeString);

                log.info("------------------------------------------------------------------------");
                log.info("Completed Create tofu resource main file. Requester : " + authEmailId);
                log.info("------------------------------------------------------------------------"); 

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
