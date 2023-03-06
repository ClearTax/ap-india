package in.clear.ap.india.activityconsolidator.util.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.redis")
@Data
public class RedisConnectionConfig {
    private String host;
    private int port;
    private short database;
}
