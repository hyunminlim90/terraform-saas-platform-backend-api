package click.opentofu.sprout.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();

        config.useSingleServer()
            .setAddress("redis://redis.opentofu.click:6379")
            .setConnectionMinimumIdleSize(10)
            .setConnectionPoolSize(20);

        return Redisson.create(config);
    }
}
