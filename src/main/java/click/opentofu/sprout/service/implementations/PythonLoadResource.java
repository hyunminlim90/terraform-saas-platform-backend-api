package click.opentofu.sprout.service.implementations;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import click.opentofu.sprout.dto.ResourceDto;
import click.opentofu.sprout.handler.python.interfaces.PythonHandler;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingle;
import click.opentofu.sprout.util.AutoCloseUtils;
import click.opentofu.sprout.util.GeneralUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PythonLoadResource implements AsyncServiceSingle {
    
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
        ResourceDto resourceDto = generalUtils.castAwsCloudRequest(dto);
        Map<String, Object> token = resourceDto.getToken();

        resourceDto.setIsNextCallable(false);
        
        return asyncWorkerSupply(
            () -> {
                try {
                    log.warn("------------------------------------------------------------------------------------------------------------------------------");
                    log.warn("AsyncServiceSingle-started for python boto3 to retrieves the configuration values of aws resources and generates a JSON file.");
                    log.warn("------------------------------------------------------------------------------------------------------------------------------");

                    String uuid = ((String) token.get("account_id")).split(",")[1];
                    String region = resourceDto.getRegionCode();
                    String awsAccessKey = (String) token.get("aws_access_key");
                    String awsSecretAccessKey = (String) token.get("aws_secret_access_key");
                    String awsSessionToken = (String) token.get("aws_session_token");
                    String beanName = "boto3_sprout";
                    String authEmailId = (String) token.get("auth_email_id");

                    Path filePath = Paths.get(ROOT_PATH, authEmailId, uuid, region, "boto3", "aws_sprout.py");
                    PythonHandler pythonHandler = pythonHandlers.get(beanName);
                    String mergeString = pythonHandler.buildAwsPythonFile(ROOT_PATH, authEmailId, uuid, region, awsAccessKey, awsSecretAccessKey, awsSessionToken);
                    pythonHandler.mainWorkerPython(filePath, mergeString);

                    log.warn("------------------------------------------");
                    log.debug("Create .py file! Requester: " + authEmailId);
                    log.warn("------------------------------------------");

                    ProcessBuilder processBuilder = new ProcessBuilder(("python3 " + filePath.toString()).split("\\s+"));
                    try (AutoCloseUtils autoCloseUtils = new AutoCloseUtils(processBuilder)) {
                        Process process = autoCloseUtils.getProcess();
                        process.waitFor();

                        try (BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                            String line;
                            log.info("▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶ [STDOUT]");
                            while ((line = stdOutReader.readLine()) != null) {
                                log.info("stdout > {}", line);
                            }
                        }

                        try (BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                            String line;
                            log.warn("⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠⚠ [STDERR]");
                            while ((line = stdErrReader.readLine()) != null) {
                                log.warn("stderr > {}", line);
                            }
                        }

                        if (process.exitValue() == 0) {
                            log.warn("----------------------------------------------------------");
                            log.warn("Completed 'python3 .py' command ! Requester: " + authEmailId);
                            log.warn("----------------------------------------------------------");
                        } else {
                            throw new RuntimeException("python_load_resource_process_exit_value");

                        }
                    } catch (Exception error) {
                        throw new RuntimeException(error);
                    }
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
