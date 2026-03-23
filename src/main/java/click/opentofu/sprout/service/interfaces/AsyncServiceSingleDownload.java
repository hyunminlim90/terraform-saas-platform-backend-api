package click.opentofu.sprout.service.interfaces;

import java.util.concurrent.CompletableFuture;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AsyncServiceSingleDownload extends AsyncService {
    default <T> CompletableFuture<InputStreamResource> mainWorkerAsync(T dto, SseEmitter emitter) { return workerSingleSupplyAsync(dto, emitter); };
    default <T> CompletableFuture<InputStreamResource> workerSingleSupplyAsync(T dto, SseEmitter emitter) { return null; };
}
