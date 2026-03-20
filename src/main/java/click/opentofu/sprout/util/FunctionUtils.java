package click.opentofu.sprout.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import click.opentofu.sprout.dto.ResourceDto;

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
}
