package in.clear.ap.india.http.util.redis;

import in.clear.ap.india.commonmodels.dtos.request.RedisFileStatusValue;

import java.util.Map;

public interface RedisService {

    String get(String key);

    void save(String key, String value, int ttlInMinutes);

    void createHash(String fileStatusKey, Map<String, RedisFileStatusValue> fileStatusMap);
}
