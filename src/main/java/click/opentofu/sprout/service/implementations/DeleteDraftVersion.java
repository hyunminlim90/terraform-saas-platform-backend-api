package click.opentofu.sprout.service.implementations;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import click.opentofu.sprout.dto.ResourceDto;
import click.opentofu.sprout.handler.entity.interfaces.BaseQueryRepository;
import click.opentofu.sprout.util.TransactionalUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteDraftVersion {

    private final TransactionalUtils transactionalUtils;

    private final Map<String, JpaRepository<?, ?>> repositoryHandlers;
 
    public void deleteResourceSaveName(ResourceDto resourceDto) {

        Map<String, Object> token = resourceDto.getToken();
        String accountId = ((String) token.get("account_id")).split(",")[0];
        String region = resourceDto.getRegionCode();
        String authUserIndex = resourceDto.getAuthUserIndex();
        String resourceSaveName = resourceDto.getResourceSaveName();
        String authEmailId = (String) token.get("auth_email_id");

        log.info("----------------------------------------------------------------------------------");
        log.info("DeleteDraftVersion-started for Deleting resource save name for account and region.");
        log.info("----------------------------------------------------------------------------------");

        String repositoryBeanName = "tofu_repository";
        JpaRepository<?, ?> repository = repositoryHandlers.get(repositoryBeanName);

        if (repository == null) {
            throw new RuntimeException("delete_draft_version_repository_null");
        }

        if (!(repository instanceof BaseQueryRepository)) {
            throw new RuntimeException("delete_draft_version_repository_instanceof_base_query_repository");
        }

        BaseQueryRepository<?> baseQueryRepository = (BaseQueryRepository<?>) repository;

        transactionalUtils.deleteResourceSaveName(baseQueryRepository, authUserIndex, accountId, region, resourceSaveName);

        Boolean result = baseQueryRepository.existsByTenantIdAndAccountIdAndRegionAndResourceSaveName(authUserIndex, accountId, region, resourceSaveName);
        if (result) { throw new RuntimeException("delete_draft_version_result_exists"); }

        log.info("-----------------------------------------------------------------------------");
        log.info("Completed Delete resource save name. Requester : " + authEmailId);
        log.info("-----------------------------------------------------------------------------");
    }
}
