package click.opentofu.sprout.service.implementations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import click.opentofu.sprout.dto.sts.TemporaryCredential;
import click.opentofu.sprout.handler.entity.interfaces.EntityHandler;
import click.opentofu.sprout.handler.entity.repository.StsRepository;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.GeneralUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JsonAndSave implements AsyncServiceSingle {

    private final TaskExecutor taskExecutor;
    private final GeneralUtils generalUtils;
    private final StsRepository stsRepository;
    private final Map<String, EntityHandler> entityHandlers;

    @Override
    @Async("taskExecutor")
    public <T> CompletableFuture<Object> mainWorkerAsync(T dto, SseEmitter unusedEmitter) {
        return AsyncServiceSingle.super.mainWorkerAsync(dto, unusedEmitter);
    }

    @Override
    public <T> CompletableFuture<Object> workerSingleSupplyAsync(T dto, SseEmitter unusedEmitter) {
        TemporaryCredential temporaryCredential = generalUtils.castTemporaryCredential(dto);
        String authEmailId = temporaryCredential.getAuthEmailId();
        String uuid = temporaryCredential.getUuid();
        temporaryCredential.setIsNextCallable(false);
        return asyncWorkerSupply(
            () -> {
                try {
                    log.warn("------------------------------------------");
                    log.warn("JsonAndSave-started for aws_sts.json ! Requester: " + authEmailId);
                    log.warn("------------------------------------------");
                    String beanName = "aws_sts_builder";
                    EntityHandler entityHandler = entityHandlers.get(beanName);
                    Path filePath = Paths.get(ROOT_PATH, "sts", authEmailId, uuid, "boto3/aws_sts.json");
                    File jsonFile = new File(filePath.toString());
                    if (!Files.exists(filePath)) {
                        throw new RuntimeException("Failed to read 'aws_sts.json' ! because the file does not exist \nFind the cause of the problem in 'service/implementations/JsonAndSave.java'");
                    }
                    JsonNode attributesNode = new ObjectMapper().readTree(jsonFile);
                    String atPath = "/outputs/aws_sts/value";
                    Set<Map.Entry<String, JsonNode>> entries = attributesNode.at(atPath).properties();
                    for (Map.Entry<String, JsonNode> entry : entries) {
                        entry.getKey();
                        JsonNode parameters = entry.getValue();
                        if (
                            "InvalidClientTokenId".equals(entry.getValue().path("account_id").asText()) ||
                            "ExpiredToken".equals(entry.getValue().path("account_id").asText()) ||
                            "UnknownException".equals(entry.getValue().path("account_id").asText())
                        ) {
                            throw new RuntimeException("Temporary credentials are invalid. from JsonAndSave.java service layer ! Requester: " + authEmailId);
                        }
                        entityHandler.buildEntityAndSaveForSts(parameters, temporaryCredential);

                    }
                    // ExpiredToken & InvalidClientTokenId 임시 자격 증명을 삭제 처리하는 첫 번째 코드.
                    stsRepository.deleteByAccountIdPrefix();
                    log.warn("------------------------------------------");
                    log.warn("ExpiredToken & InvalidClientTokenId records have been successfully deleted from JsonAndSave.java service layer ! Requester: " + authEmailId);
                    log.warn("------------------------------------------");
                } catch (IOException error) {
                    throw new RuntimeException("Failed to complete I/O operations in the JsonAndSave Service class");
                }
                temporaryCredential.setIsNextCallable(true);
                return "success";
            },
            taskExecutor
        );
    }
}
