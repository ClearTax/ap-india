package in.clear.ap.india.http.util.s3;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

import java.net.URI;

@Configuration
@ConfigurationProperties(prefix = "aws.s3")
@Data
public class S3ClientConfigurarionProperties {

    private Region region = Region.AP_SOUTH_1;
    private URI endpoint = null;

    private String accessKeyId;
    private String secretAccessKey;

    // Bucket name we'll be using as our backend storage
    private String bucket;

    // AWS S3 requires that file parts must have at least 5MB, except
    // for the last part. This may change for other S3-compatible services, so let't
    // define a configuration property for that
    private int multipartMinPartSize = 5 * 1024 * 1024;

    private int signatureDurationForPut = 600; // ten mins

}
