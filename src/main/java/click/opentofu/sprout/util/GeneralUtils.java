package click.opentofu.sprout.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.zeroturnaround.zip.ZipUtil;

import click.opentofu.sprout.dto.request.ResourceDto;
import click.opentofu.sprout.dto.sts.AwsCloudRequest;
import click.opentofu.sprout.dto.sts.TemporaryCredential;
import click.opentofu.sprout.service.interfaces.AsyncService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GeneralUtils {

    public String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) { 
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void beanExists (
        AsyncService asyncService,
        String serviceName
    ) {
        if (asyncService == null) {
            throw new RuntimeException("bean_exists");
        }
    }

    public void isAuthorizedForWrite(
        List<String> roles,
        String authEmailId
    ) {
        if (!roles.contains("write")) {
            log.warn("----------------------------------------------------------------");
            log.warn("You do not have the 'write' permission ! Requester: " + authEmailId);
            log.warn("----------------------------------------------------------------");
            throw new RuntimeException("is_authorized_for_write");
        }

        List<String> allowedEmailIds = List.of(
            "test1"
        );
    
        if (!allowedEmailIds.contains(authEmailId)) {
            throw new RuntimeException("general_utils_validate_allowed_email_id_unauthorized_email");
        }
    }

    public void isAuthorizedForRead(
        List<String> roles,
        String authEmailId
    ) {
        if (!roles.contains("read")) {
            log.warn("------------------------------------------");
            log.warn("You do not have the 'read' permission ! Requester: " + authEmailId);
            log.warn("------------------------------------------");
            throw new RuntimeException("You do not have the 'read' permission ! Requester: " + authEmailId + "\nPlease check in the requestAwsAccounts() method.");
        }
    }

    public List<String> castToListOfString (
        Object object
    ) {
        if (object instanceof List<?>) {
            return ((List<?>) object).stream()
                .filter((item) -> { return item instanceof String; })
                .map((item) -> { return (String) item; })
                .collect(Collectors.toList());
        };
        return Collections.emptyList();
    }

    public String castToString (
        Object object
    ) {
        if (object instanceof String) {
            return (String) object;
        };
        return "";
    }

    public <T> ResourceDto castAwsCloudRequest (
        T dto
    ) {
        if (dto instanceof ResourceDto) {
            return (ResourceDto) dto;
        } else {
            return null;
        }
    }

    public void deleteDirectoryRecursively(Path directory) {
        if (Files.exists(directory)) {
            try (Stream<Path> paths = Files.walk(directory)) {
                paths
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (Exception error) {
                            throw new RuntimeException(error);
                        }
                    });
            } catch (Exception error) {
                throw new RuntimeException(error);
            }
        }
    }

    public void deleteSubdirectoryContents(Path directory) throws IOException {
        if (Files.exists(directory)) {
            Files.list(directory)
                .forEach(
                    (path) -> {
                        try {
                            if (Files.isDirectory(path)) {
                                deleteDirectoryRecursively(path);
                            } else {
                                Files.delete(path);
                            }
                        } catch (IOException error) {
                            throw new RuntimeException("Failed to delete: " + path + ". The location of the class is util/GeneralUtils.java (method: deleteSubdirectoryContents)", error);
                        }
                    }
                );
        }
    }

    public <T> TemporaryCredential castTemporaryCredential (
        T dto
    ) {
        if (dto instanceof TemporaryCredential) {
            return (TemporaryCredential) dto;
        } else {
            return null;
        }
    }

    public <T> AwsCloudRequest castAwsCloudRequestForSts (
        T dto
    ) {
        if (dto instanceof AwsCloudRequest) {
            return (AwsCloudRequest) dto;
        } else {
            return null;
        }
    }

    public void compressDirectoryToZip(
        String sourceDirPath,
        String targetZipFileName
    ) {
        File sourceDir = new File(sourceDirPath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new RuntimeException("The path is not a directory or cannot be found. path: " + sourceDir.getAbsolutePath() + ". The location of the class is util/GeneralUtils.java (method: generateZipFileStream)");
        }
        File targetZipFile = new File(targetZipFileName);
        try {
            ZipUtil.pack(sourceDir, targetZipFile);
        } catch (Exception error) {
            throw new RuntimeException("Failed to compress the directory. The location of the class is util/GeneralUtils.java (method: compressDirectoryToZip)", error);
        }
    }

    public InputStreamResource generateZipFileStream(String targetZipFileName) {
        File targetZipFile = new File(targetZipFileName);
        try {
            return new InputStreamResource(new FileInputStream(targetZipFile));
        } catch (FileNotFoundException error) {
            throw new RuntimeException("Failed to find the file. The location of the class is util/GeneralUtils.java (method: generateZipFileStream)", error);
        }
    }
}
