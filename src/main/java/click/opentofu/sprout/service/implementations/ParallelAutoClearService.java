package click.opentofu.sprout.service.implementations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import click.opentofu.sprout.constants.AppConstants;
import click.opentofu.sprout.dto.sts.AccountRequest;
import click.opentofu.sprout.handler.entity.repository.StsRepository;
import click.opentofu.sprout.handler.python.interfaces.PythonHandler;
import click.opentofu.sprout.util.AutoCloseUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParallelAutoClearService {

    String ROOT_PATH = AppConstants.ROOT_PATH;

    private final Map<String, PythonHandler> pythonHandlers;
    private final StsRepository stsRepository;
    
    @Transactional
    public void parallelAutoClear (AccountRequest accountRequest) {
        String authEmailId = accountRequest.getAuthEmailId();
        log.warn("------------------------------------------");
        log.warn("stsEntity: ");
        log.warn(accountRequest.toString());
        log.warn("Requester: " + authEmailId);
        log.warn("------------------------------------------");
        try {
            String uuid = accountRequest.getAccountId().split(",")[1];
            String awsAccessKey = accountRequest.getAwsAccessKey();
            String awsSecretAccessKey = accountRequest.getAwsSecretAccessKey();
            String awsSessionToken = accountRequest.getAwsSessionToken();
            String beanName = "boto3_sts_verify";
            log.warn("------------------------------------------");
            log.warn("ParallelAutoClearService.parallelAutoClear - started for Python boto3 to delete expired tokens");
            log.warn("------------------------------------------");

            Path filePath = Paths.get(ROOT_PATH, "sts", authEmailId, uuid, "boto3", "aws_sts_verify.py");

            PythonHandler pythonHandler = pythonHandlers.get(beanName);
            String mergeString = pythonHandler.buildAwsPythonFileForSts(ROOT_PATH, uuid, authEmailId, awsAccessKey, awsSecretAccessKey, awsSessionToken);
            pythonHandler.mainWorkerPython(filePath, mergeString);
            log.warn("------------------------------------------");
            log.debug("Create file: aws_sts_verify.py ! Requester: " + authEmailId);
            log.warn("------------------------------------------");
            ProcessBuilder processBuilder = new ProcessBuilder(("python3 " + filePath.toString()).split("\\s+"));
            try (AutoCloseUtils autoCloseUtils = new AutoCloseUtils(processBuilder)) {
                Process process = autoCloseUtils.getProcess();
                process.waitFor();
                if (process.exitValue() == 0) {
                    log.warn("------------------------------------------");
                    log.warn("Completed 'python aws_sts_verify.py' command ! (Token is valid)  Requester: " + authEmailId);
                    log.warn("------------------------------------------");
                } else {
                    log.warn("------------------------------------------");
                    log.warn("Completed 'python aws_sts_verify.py' command ! (Token is invalid)  Requester: " + authEmailId);
                    log.warn("------------------------------------------");
                    stsRepository.deleteByAccountId(accountRequest.getAccountId());
                }
            } catch (Exception error) {
                throw new RuntimeException("Failed to execute 'python aws_sts_verify.py' due to an unexpected exception \nFind the cause of the problem in '/service/normal/ParallelAutoClearService.java'", error);
            }
        } catch (IOException error) {
            throw new RuntimeException("Failed to complete I/O operations in the ParallelAutoClearService Service class", error);
        }

        try {
            log.warn("------------------------------------------");
            log.warn("ParallelAutoClearService.parallelAutoClear-started for delete sts_verify utils ! Requester: " + authEmailId);
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
            throw new RuntimeException("Failed to execute delete root path due to an unexpected exception \nFind the cause of the problem in 'service/normal/ParallelAutoClearService.java'", error);
        }
    }
}
