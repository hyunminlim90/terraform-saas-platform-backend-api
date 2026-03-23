package click.opentofu.sprout.service.implementations;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import click.opentofu.sprout.constants.AppConstants;
import click.opentofu.sprout.handler.entity.repository.StsRepository;
import click.opentofu.sprout.util.GeneralUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteService {
    
    private final StsRepository stsRepository;
    private final GeneralUtils generalUtils;

    String ROOT_PATH = AppConstants.ROOT_PATH;

    @Transactional
    public void deleteAwsAccount (
        String accountId,
        String authEmailId
    ) {
        try {
            log.warn("------------------------------------------");
            log.warn("DeleteService.deleteAwsAccount - started for deletion of AWS account based on user's selection ! Requester: " + authEmailId);
            log.warn("------------------------------------------");
            stsRepository.deleteByAccountId(accountId);
            log.warn("------------------------------------------");
            log.warn("Completed for deletion of AWS account based on user's selection ! Requester: " + authEmailId);
            log.warn("------------------------------------------");
        } catch (Exception error) {
            throw new RuntimeException("Failed to delete AWS account, in the deleteAwsAccount Method. Class is DeleteService ! Requester: " + authEmailId);
        }
    }

    public void deleteAwsWorkspace (String authEmailId) {
        try {
            log.warn("------------------------------------------");
            log.warn("DeleteService.deleteAwsWorkspace - Task-started for deletion of AWS workspace based on user's selection ! Requester: " + authEmailId);
            log.warn("------------------------------------------");

            Path workspaceDirPath = Paths.get(ROOT_PATH, authEmailId);
            try {
                generalUtils.deleteSubdirectoryContents(workspaceDirPath);
                log.info("Successfully deleted directory: " + workspaceDirPath.toString());
            } catch (IOException error) {
                throw new RuntimeException("Failed to delete: " + workspaceDirPath.toString() + ". The location of the class is service/normal/DeleteService.java");
            }
            log.warn("------------------------------------------");
            log.warn("Completed for discard of AWS workspace based on user's selection ! Requester: " + authEmailId);
            log.warn("------------------------------------------");
        } catch (Exception error) {
            throw new RuntimeException("Failed to discard AWS workspace, in the deleteAwsWorkspace Method. Class is DeleteService ! Requester: " + authEmailId);
        }
    }
}
