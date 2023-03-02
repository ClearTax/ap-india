package in.clear.ap.india.http.util.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.util.StringUtils;
import org.springframework.lang.Nullable;

public class S3ClientSingleton {

    private static S3ClientSingleton single_instance = null;

    AmazonS3 amazonS3;

    private S3ClientSingleton(@Nullable String accessKey, @Nullable String secretKey, @Nullable String region,
            String defaultRegion) {
        if (StringUtils.isNullOrEmpty(accessKey) || StringUtils.isNullOrEmpty(secretKey)) {
            amazonS3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                    .withRegion(StringUtils.isNullOrEmpty(region) ? defaultRegion : region).build();
        } else {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
            amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withRegion(StringUtils.isNullOrEmpty(region) ? defaultRegion : region).build();
        }
    }

    public static S3ClientSingleton getBuildClientInstance(@Nullable String accessKey, @Nullable String secretKey,
            @Nullable String region, String defaultRegion) {
        if (single_instance == null) {
            single_instance = new S3ClientSingleton(accessKey, secretKey, region, defaultRegion);
        }

        return single_instance;
    }
}
