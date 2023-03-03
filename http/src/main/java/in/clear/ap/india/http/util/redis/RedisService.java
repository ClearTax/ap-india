package in.clear.ap.india.http.util.redis;

public interface RedisService {

    String get(String key);

    void save(String key, String value, int ttlInMinutes);
}
