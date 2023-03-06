package in.clear.ap.india.activityconsolidator.util.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class ConnectionFactoryConfig {

    private final RedisConnectionConfig redisConnectionConfig;

    public ConnectionFactoryConfig(RedisConnectionConfig redisConnectionConfig) {
        this.redisConnectionConfig = redisConnectionConfig;
    }

    @Bean
    public LettuceConnectionFactory reactiveRedisConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConnectionConfig.getHost(),
                redisConnectionConfig.getPort());
        if (redisConnectionConfig.getDatabase() > 0) {
            factory.setDatabase(redisConnectionConfig.getDatabase());
        }
        return factory;
    }
}
