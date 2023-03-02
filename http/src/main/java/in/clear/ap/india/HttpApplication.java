package in.clear.ap.india;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import in.clear.ap.india.http.services.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@SpringBootApplication
@EnableFeignClients("in.clear")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HttpApplication {

    private final ActivityService activityService;

    public static void main(String[] args) {
        SpringApplication.run(HttpApplication.class, args);
    }

    @Scheduled(fixedDelay = 5)
    public void expireDelayedActivities(){
        activityService.expireUncompletedActivities();
    }

    @Bean
    public AmazonS3Client getAmazonS3Client() {
        return (AmazonS3Client) AmazonS3Client.builder()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).withRegion(Regions.AP_SOUTH_1)
                .build();
    }

    @Bean
    public SQSConnectionFactory sqsConnectionFactory() {
        log.info("Creating SQS Connection Factory");
        log.info(DefaultAWSCredentialsProviderChain.getInstance().getCredentials().toString());
        return new SQSConnectionFactory(new ProviderConfiguration(), AmazonSQSClientBuilder.standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).withRegion(Regions.AP_SOUTH_1));
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

}
