package click.opentofu.sprout.service.implementations;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;

import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.GeneralUtils;
import click.opentofu.sprout.dto.ResourceDto;
import click.opentofu.sprout.handler.entity.interfaces.BaseQueryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExistsResourceSaveName implements AsyncServiceSingle {

    private final TaskExecutor taskExecutor;
    private final GeneralUtils generalUtils;

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
        String authEmailId = (String) token.get("auth_email_id");
        String resourceSaveName = resourceDto.getResourceSaveName();
        
        resourceDto.setIsNextCallable(false);

        return asyncWorkerSupply(
            () -> {
                log.info("----------------------------------------------------------------------");
                log.info("AsyncServiceSingle-started for checking if resource save name exists.");
                log.info("----------------------------------------------------------------------");

                String repositoryBeanName = "tofu_repository";
                JpaRepository<?, ?> repository = repositoryHandlers.get(repositoryBeanName);

                if (repository == null) {
                    throw new RuntimeException("exists_resource_save_name_repository_null");
                }

                if (!(repository instanceof BaseQueryRepository)) {
                    throw new RuntimeException("exists_resource_save_name_repository_instanceof_base_query_repository");
                }

                BaseQueryRepository<?> baseQueryRepository = (BaseQueryRepository<?>) repository;
                Boolean result = baseQueryRepository.existsByTenantIdAndAccountIdAndRegionAndResourceSaveName(authUserIndex, accountId, region, resourceSaveName);

                if (result) { throw new RuntimeException("exists_resource_save_name_result_exists"); }

                log.info("-----------------------------------------------------------------------------");
                log.info("Completed checking resource save name exists. Requester : " + authEmailId);
                log.info("-----------------------------------------------------------------------------");

                resourceDto.setIsNextCallable(true);
                return new Object();
            },
            taskExecutor
        );  
    }
}
