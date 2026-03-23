package click.opentofu.sprout.service.interfaces;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.task.TaskExecutor;

import click.opentofu.sprout.constants.AppConstants;

public interface AsyncService {

    Integer TIMEOUT_SECONDS_MULTI = AppConstants.TIMEOUT_SECONDS_MULTI;
    Integer TIMEOUT_SECONDS_SINGLE = AppConstants.TIMEOUT_SECONDS_SINGLE;
    String ROOT_PATH = AppConstants.ROOT_PATH;
    String DOWNLOAD_ROOT_PATH = AppConstants.DOWNLOAD_ROOT_PATH;

    Map<String, List<String>> TEXTAREA_PARAMS_BY_MODULE = AppConstants.TEXTAREA_PARAMS_BY_MODULE;

    default public <T> CompletableFuture<Object> asyncWorkerSupply (
        Supplier<Object> logicFunction,
        TaskExecutor taskExecutor
    ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return logicFunction.get();
            }, taskExecutor
        );
    }

    default public <T> CompletableFuture<InputStreamResource> asyncWorkerSupplyDownload (
        Supplier<InputStreamResource> logicFunction,
        TaskExecutor taskExecutor
    ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return logicFunction.get();
            }, taskExecutor
        );
    }
}
