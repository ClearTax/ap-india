package in.clear.ap.india;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootApplication
public class UnstructuredInputConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnstructuredInputConsumerApplication.class, args);
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
