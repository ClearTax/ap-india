package in.clear.ap.india.http.util.sqs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

import java.net.URI;

@Configuration
@ConfigurationProperties(prefix = "aws.sqs")
@Data
public class SqsConfig {

    private String region = String.valueOf(Region.AP_SOUTH_1);
    private URI endpoint = null;

    private String accessKeyId;
    private String secretAccessKey;
}
