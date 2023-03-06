package in.clear.ap.india.http.util.redis;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@Slf4j
public class RedisServiceImplementation implements RedisService {
    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;
    private ReactiveValueOperations<String, String> reactiveValueOps;

    @PostConstruct
    public void setup() {
        reactiveValueOps = redisTemplate.opsForValue();
    }

    @Override
    public String get(String key) {
        return reactiveValueOps.get(key).share().block();
    }

    @Override
    public void save(String key, String value, int ttlInMinutes) {
        reactiveValueOps.set(key, value, Duration.ofMinutes(ttlInMinutes)).share().block();
    }

    @Override
    public void createHash(String fileStatusKey, Map<String, String> fileStatusMap) {
        ReactiveHashOperations<String,String,String> reactiveHashOperations = redisTemplate.opsForHash();
        for(Map.Entry<String,String> entry : fileStatusMap.entrySet()){
            reactiveHashOperations.put(fileStatusKey, entry.getKey(), entry.getValue()).share().block();
        }
    }
}
