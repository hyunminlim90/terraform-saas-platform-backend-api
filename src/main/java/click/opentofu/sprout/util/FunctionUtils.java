package click.opentofu.sprout.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import click.opentofu.sprout.dto.request.ResourceDto;
import click.opentofu.sprout.dto.sts.TemporaryCredential;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FunctionUtils {
    
    private final TaskExecutor taskExecutor;
    
    public CompletableFuture<ResponseEntity<Map<String, Object>>> asyncChain (
        ResourceDto resourceDto,
        Supplier<CompletableFuture<Object>> mainWorkerAsyncFunction,
        Supplier<CompletableFuture<ResponseEntity<Map<String, Object>>>> nextStepFunction
    ) {
        CompletableFuture<Object> completableFuture = mainWorkerAsyncFunction.get();
        return asyncResponse(completableFuture)
            .thenCompose(
                (responseEntity) -> {
                    if (resourceDto.getIsNextCallable()) {
                        return nextStepFunction.get();
                    } else {
                        return CompletableFuture.completedFuture(responseEntity);
                    }
                }
            )
            .exceptionally((exception) -> {
                return ExceptionUtils.handleAsyncException(exception); 
            });
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> asyncResponse (
        CompletableFuture<Object> completableFuture
    ) {
        return completableFuture.thenApplyAsync(
            (prev) -> {
                Map<String, Object> response = new HashMap<>();
                if (prev != null && prev.getClass() == Object.class) {
                    response.put("result", "Operation succeeded");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("result", prev);
                    return ResponseEntity.ok().body(response);
                }
            }, taskExecutor
        );
    }

    public CompletableFuture<ResponseEntity<Map<String, Object>>> asyncChainForSts (
        TemporaryCredential temporaryCredential,
        Supplier<CompletableFuture<Object>> mainWorkerAsyncFunction,
        Supplier<CompletableFuture<ResponseEntity<Map<String, Object>>>> nextStepFunction
    ) {
        CompletableFuture<Object> completableFuture = mainWorkerAsyncFunction.get();
        return asyncResponseForSts(completableFuture)
            .thenCompose(
                (responseEntity) -> {
                    if (temporaryCredential.getIsNextCallable()) {
                        return nextStepFunction.get();
                    } else {
                        return CompletableFuture.completedFuture(responseEntity);
                    }
                }
            );
    }

    private CompletableFuture<ResponseEntity<Map<String, Object>>> asyncResponseForSts (
        CompletableFuture<Object> completableFuture
    ) {
        return completableFuture.thenApplyAsync(
            (prev) -> {
                Map<String, Object> response = new HashMap<>();
                if ("success".equals(prev)) {
                    response.put("resultMessage", "Operation succeeded");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("resultMessage", "Operation failed");
                    return ResponseEntity.badRequest().body(response);
                }
            }, taskExecutor
        );
    }

    public CompletableFuture<ResponseEntity<InputStreamResource>> asyncDownloadResponse (
        CompletableFuture<InputStreamResource> completableFuture,
        String zipFileName
    ) {
        return completableFuture.thenApplyAsync(
            (prev) -> {
                if (prev != null) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFileName);
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    return ResponseEntity.ok().headers(headers).body(prev);
                } else {
                    throw new RuntimeException("Failed to compress the file: " + zipFileName + " The location of the class is service/implementations/TerraformDownloadVpc.java");
                }
            }, taskExecutor
        );
    }
}
