package click.opentofu.sprout.service.implementations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import click.opentofu.sprout.dto.sts.TemporaryCredential;
import click.opentofu.sprout.handler.python.interfaces.PythonHandler;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.AutoCloseUtils;
import click.opentofu.sprout.util.GeneralUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PythonBoto implements AsyncServiceSingle {

    private final TaskExecutor taskExecutor;
    private final GeneralUtils generalUtils;
    private final Map<String, PythonHandler> pythonHandlers;

    @Override
    @Async("taskExecutor")
    public <T> CompletableFuture<Object> mainWorkerAsync(T dto, SseEmitter unusedEmitter) {
        return AsyncServiceSingle.super.mainWorkerAsync(dto, unusedEmitter);
    }

    @Override
    public <T> CompletableFuture<Object> workerSingleSupplyAsync(T dto, SseEmitter unusedEmitter) {
        TemporaryCredential temporaryCredential = generalUtils.castTemporaryCredential(dto);
        String authEmailId = temporaryCredential.getAuthEmailId();
        String awsAccessKey = temporaryCredential.getAwsAccessKey();
        String awsSecretAccessKey = temporaryCredential.getAwsSecretAccessKey();
        String awsSessionToken = temporaryCredential.getAwsSessionToken();
        String uuid = temporaryCredential.getUuid();
        temporaryCredential.setIsNextCallable(false);
        return asyncWorkerSupply(
            () -> {
                try {
                    log.warn("------------------------------------------");
                    log.warn("AsyncServiceSingle-Task-started for python boto3 to retrieving AWS Account ID with Temporary Credentials");
                    log.warn("------------------------------------------");
                    String beanName = "boto3_sts";
                    Path filePath = Paths.get(ROOT_PATH, "sts", authEmailId, uuid, "boto3", "aws_sts.py");
                    PythonHandler pythonHandler = pythonHandlers.get(beanName);
                    String mergeString = pythonHandler.buildAwsPythonFileForSts(ROOT_PATH, uuid, authEmailId, awsAccessKey, awsSecretAccessKey, awsSessionToken);
                    pythonHandler.mainWorkerPython(filePath, mergeString);
                    log.warn("------------------------------------------");
                    log.debug("Create file: aws_sts.py ! Requester: " + authEmailId);
                    log.warn("------------------------------------------");
                    ProcessBuilder processBuilder = new ProcessBuilder(("python3 " + filePath.toString()).split("\\s+"));
                    try (AutoCloseUtils autoCloseUtils = new AutoCloseUtils(processBuilder)) {
                        Process process = autoCloseUtils.getProcess();

                        try (BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                            String line;
                            log.info("▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶ [STDOUT]");
                            while ((line = stdOutReader.readLine()) != null) {
                                log.info("stdout > {}", line);
                            }
                        }

                        try (BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                            String line;
                            log.warn("⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠ [STDERR]");
                            while ((line = stdErrReader.readLine()) != null) {
                                log.warn("stderr > {}", line);
                            }
                        }

                        process.waitFor();
                        if (process.exitValue() == 0) {
                            log.warn("------------------------------------------");
                            log.warn("Completed 'python aws_sts.py' command ! Requester: " + authEmailId);
                            log.warn("------------------------------------------");
                        } else {
                            throw new RuntimeException("Failed to complete the process Exit code: " + process.exitValue() + "\nFind the cause of the problem in '/service/implementations/PythonBoto.java'");
                        }
                    } catch (Exception error) {
                        throw new RuntimeException("Failed to execute 'python aws_sts.py' due to an unexpected exception \nFind the cause of the problem in '/service/implementations/PythonBoto.java'");
                    }
                } catch (IOException error) {
                    throw new RuntimeException("Failed to complete I/O operations in the PythonBoto Service class");
                }
                temporaryCredential.setIsNextCallable(true);
                return "success";
            },
            taskExecutor
        );
    }
}
