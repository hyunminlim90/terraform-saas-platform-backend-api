package click.opentofu.sprout.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import click.opentofu.sprout.dto.request.ResourceDto;
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
}
