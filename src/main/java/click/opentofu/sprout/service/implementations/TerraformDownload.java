package click.opentofu.sprout.service.implementations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import click.opentofu.sprout.dto.sts.AwsCloudRequest;
import click.opentofu.sprout.service.interfaces.AsyncServiceSingleDownload;
import click.opentofu.sprout.util.GeneralUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TerraformDownload implements AsyncServiceSingleDownload {
    
    private final TaskExecutor taskExecutor;
    private final GeneralUtils generalUtils;

    @Override
    @Async("taskExecutor")
    public <T> CompletableFuture<InputStreamResource> mainWorkerAsync(T dto, SseEmitter unusedEmitter) {
        return AsyncServiceSingleDownload.super.mainWorkerAsync(dto, unusedEmitter);
    }

    @Override
    public <T> CompletableFuture<InputStreamResource> workerSingleSupplyAsync(T dto, SseEmitter unusedEmitter) {
        AwsCloudRequest awsCloudRequest = generalUtils.castAwsCloudRequestForSts(dto);
        List<Map<String, Object>> selectedAccountObjectList = awsCloudRequest.getSelectedAccountObjectList();
        String authEmailId = (String) selectedAccountObjectList.get(0).get("authEmailId");
        String scriptType = awsCloudRequest.getSelectedScriptTypeValue();
        List<String> selectedAccountList = selectedAccountObjectList.stream()
            .map((data) -> { return (String) data.get("accountId") + "," + (String) data.get("alias"); } )
            .distinct()
            .collect(Collectors.toList());
        if (selectedAccountList.isEmpty()) {
            throw new RuntimeException("No selected accountId. The location of the class is service/implementations/TerraformDownload.java");
        }
        awsCloudRequest.setIsNextCallable(false);
        Path deleteDownloadDirPath = Paths.get(DOWNLOAD_ROOT_PATH, authEmailId);
        Path deleteWorkspaceDirPath = Paths.get(ROOT_PATH, authEmailId);
        Path finalCompressPath = Paths.get(DOWNLOAD_ROOT_PATH, authEmailId, "compress");
        Path finalTargetPath = Paths.get(DOWNLOAD_ROOT_PATH, authEmailId, "target");
        Path targetFilePath = Paths.get(DOWNLOAD_ROOT_PATH, authEmailId, "target", "terraform-module-download.zip");
        return asyncWorkerSupplyDownload(
            () -> {
                try {
                    Files.createDirectories(finalCompressPath);
                    Files.createDirectories(finalTargetPath);
                } catch (IOException error) {
                    throw new RuntimeException("Failed to create the directory. The location of the class is service/implementations/TerraformDownload.java");
                }
                for (String selectedAccount : selectedAccountList) {
                    Path sourceDirPath = Paths.get(ROOT_PATH, authEmailId, selectedAccount.split(",")[1]);
                    Path targetZipFileName = Paths.get(DOWNLOAD_ROOT_PATH, authEmailId, "compress", selectedAccount.split(",")[2]);
                    generalUtils.compressDirectoryToZip(sourceDirPath.toString() , targetZipFileName.toString() + ".zip");
                }
                if (scriptType.equals("GitLab")) {
                    Path sourceDirPath = Paths.get(ROOT_PATH, authEmailId, "components");
                    Path targetZipFileName = Paths.get(DOWNLOAD_ROOT_PATH, authEmailId, "compress", "components");
                    generalUtils.compressDirectoryToZip(sourceDirPath.toString(), targetZipFileName.toString() + ".zip");
                }
                generalUtils.compressDirectoryToZip(finalCompressPath.toString(), targetFilePath.toString());
                InputStreamResource zipFileStream = generalUtils.generateZipFileStream(targetFilePath.toString());
                awsCloudRequest.setIsNextCallable(true); 
                return zipFileStream;
            },
            taskExecutor
        )
        .whenComplete(
            (result, throwable) -> {
                CompletableFuture.runAsync(
                    () -> {
                        try {
                            generalUtils.deleteDirectoryRecursively(deleteDownloadDirPath);
                            generalUtils.deleteDirectoryRecursively(deleteWorkspaceDirPath);
                            log.info("Successfully deleted directory: " + finalCompressPath.toString());
                        } catch (Exception error) {
                            throw new RuntimeException("Failed to delete: " + finalCompressPath.toString() + ". The location of the class is service/implementations/TerraformDownload.java");
                        }
                    }
                );
            }
        );
    }
}
