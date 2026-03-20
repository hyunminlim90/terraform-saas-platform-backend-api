package click.opentofu.sprout.service.interfaces;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AsyncServiceSingle extends AsyncService {
    default <T> CompletableFuture<Object> mainWorkerAsync(T dto, SseEmitter emitter) { return workerSingleSupplyAsync(dto, emitter); };
    default <T> CompletableFuture<Object> workerSingleSupplyAsync(T dto, SseEmitter emitter) { return null; };
}
