package click.opentofu.sprout.service.implementations;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import click.opentofu.sprout.dto.ResourceDto;
import click.opentofu.sprout.dto.TofuDto;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.GeneralUtils;
import click.opentofu.sprout.util.TransactionalUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoadDraftVersion implements AsyncServiceSingle {
    
    private final TaskExecutor taskExecutor;
    private final GeneralUtils generalUtils;
    private final TransactionalUtils transactionalUtils;

    private final Map<String, JpaRepository<?, ?>> repositoryHandlers;

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
        
        resourceDto.setIsNextCallable(false);

        return asyncWorkerSupply(
            () -> {
                log.info("------------------------------------------------------------");
                log.info("AsyncServiceSingle-started to fetch Draft Version entities.");
                log.info("------------------------------------------------------------");

                String repositoryBeanName = "tofu_repository";
                JpaRepository<?, ?> repository = repositoryHandlers.get(repositoryBeanName);

                if (repository == null) {
                    throw new RuntimeException("load_draft_version_repository_null");
                }

                List<TofuDto> result = transactionalUtils.fetchWithLazyInit(repository, authUserIndex, accountId, region);

                return result;
            },
            taskExecutor
        );  
    }
}
