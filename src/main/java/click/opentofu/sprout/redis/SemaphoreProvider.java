package click.opentofu.sprout.redis;

import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SemaphoreProvider {

    private final RedissonClient redissonClient;

    public RSemaphore getPlanSemaphore() {
        return redissonClient.getSemaphore("tofu:plan");
    }

    public RSemaphore getApplySemaphore() {
        return redissonClient.getSemaphore("tofu:apply");
    }

    public RSemaphore getDestroySemaphore() {
        return redissonClient.getSemaphore("tofu:destroy");
    }
}
