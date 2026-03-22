package click.opentofu.sprout.service.implementations;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import click.opentofu.sprout.dto.request.ResourceDto;
import click.opentofu.sprout.handler.entity.interfaces.BaseQueryRepository;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.GeneralUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExistsDraftVersion implements AsyncServiceSingle {
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
        List<List<Map<String, Object>>> duplicateDraftVersion = resourceDto.getDuplicateDraftVersion();

        String moduleName = resourceDto.getModuleName();

        resourceDto.setIsNextCallable(false);

        return asyncWorkerSupply(
            () -> {
                log.info("------------------------------------------------------------------------------------");
                log.info("AsyncServiceSingle-started to check if resource save name exists before duplication.");
                log.info("------------------------------------------------------------------------------------");

                List<String> resourceSaveNames = duplicateDraftVersion.stream()
                    .filter((innerList) -> !innerList.isEmpty())
                    .map((innerList) -> (String) innerList.get(0).get("resource_save_name"))
                    .collect(Collectors.toList());

                String repositoryBeanName = moduleName.substring("aws_".length()) + "_repository";
                JpaRepository<?, ?> repository = repositoryHandlers.get(repositoryBeanName);

                if (repository == null) {
                    throw new RuntimeException("exists_draft_version_repository_null");
                }

                if (!(repository instanceof BaseQueryRepository)) {
                    throw new RuntimeException("exists_draft_version_repository_instanceof_base_query_repository");
                }

                BaseQueryRepository<?> baseQueryRepository = (BaseQueryRepository<?>) repository;
                List<String> result = baseQueryRepository.findExistingResourceSaveNames(authUserIndex, accountId, region, moduleName, resourceSaveNames);

                if (!result.isEmpty()) { return result; }

                log.info("----------------------------------------------------------------------------------------");
                log.info("Completed checking resource save name exists before duplication. Requester : " + authEmailId);
                log.info("----------------------------------------------------------------------------------------");

                resourceDto.setIsNextCallable(true);
                return new Object();
            },
            taskExecutor
        );  
    }
}
