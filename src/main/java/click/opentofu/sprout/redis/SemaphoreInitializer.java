package click.opentofu.sprout.redis;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SemaphoreInitializer {

    private final RedissonClient redissonClient;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        RSemaphore planSemaphore = redissonClient.getSemaphore("tofu:plan");
        planSemaphore.trySetPermits(6);

        RSemaphore applySemaphore = redissonClient.getSemaphore("tofu:apply");
        applySemaphore.trySetPermits(4);

        RSemaphore destroySemaphore = redissonClient.getSemaphore("tofu:destroy");
        destroySemaphore.trySetPermits(3);
    }
}
