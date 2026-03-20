package click.opentofu.sprout.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class EmitterUtils {
    
    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitter.onTimeout(() -> { removeEmitter(emitter); });
        emitter.onCompletion(() -> { removeEmitter(emitter); });
        return emitter;
    }

    public String registerNewEmitter(SseEmitter emitter) {
        String uniqueId = UUID.randomUUID().toString();
        sseEmitters.put(uniqueId, emitter);
        return uniqueId;
    }

    public SseEmitter getEmitterByUniqueId(String uniqueId) {
        return sseEmitters.get(uniqueId);
    }

    public void emitterExists (SseEmitter emitter, String uniqueId) {
        if (emitter == null) { throw new RuntimeException("emitter_exists"); }
    }

    private void removeEmitter(SseEmitter emitter) {
        sseEmitters.entrySet()
            .removeIf((entry) -> { return entry.getValue().equals(emitter); });
    }
}
