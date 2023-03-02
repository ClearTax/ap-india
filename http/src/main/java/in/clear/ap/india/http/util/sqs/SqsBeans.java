package in.clear.ap.india.http.util.sqs;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.utils.StringUtils;

@Configuration
public class SqsBeans {

    @Bean(name = "SqsService")
    @Primary
    public AmazonSQS buildSQSClient(SqsConfig sqsConfig) {

        if (StringUtils.isNotBlank(sqsConfig.getAccessKeyId())
                && StringUtils.isNotBlank(sqsConfig.getSecretAccessKey())) {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(sqsConfig.getAccessKeyId(),
                    sqsConfig.getSecretAccessKey());
            return AmazonSQSClient.builder().withRegion(sqsConfig.getRegion())
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
        }

        return AmazonSQSClient.builder().withRegion(sqsConfig.getRegion())
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).build();

    }
}
