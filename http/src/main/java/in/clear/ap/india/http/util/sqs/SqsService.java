package in.clear.ap.india.http.util.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SqsService {

    private final AmazonSQS sqsClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SendMessageResult publish(String message, String queueUrl) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(queueUrl)
                .withMessageBody(message);

        log.debug("Publishing SQS message[{}] to queue[{}]", message, queueUrl);
        return sqsClient.sendMessage(sendMessageRequest);
    }

    public void publish(Object messageDto, String queueUrl) {
        if (Objects.isNull(messageDto)) {
            log.info("Message to publish to SQS is null. Skipping publish. queueUrl[{}]", queueUrl);
            return;
        }
        try {
            String serializedMessage = getSerializedMessage(messageDto);
            this.publish(serializedMessage, queueUrl);
        } catch (Exception e) {
            log.error("Exception found while publishing SQS message[{}] to queue[{}]", messageDto, queueUrl, e);
            throw e;
        }
    }

    public Message receiveMessage(String queueUrl) {
        return this.receiveMessage(queueUrl, 1).get(0);
    }

    public List<Message> receiveMessage(String queueUrl, int noOfMessage) {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl).withWaitTimeSeconds(10)
                .withMaxNumberOfMessages(noOfMessage);
        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();
        return messages;
    }

    private String getSerializedMessage(Object message) {
        String serializedMessage = null;

        try {
            serializedMessage = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error("Error while serializing message", e);
        }

        return serializedMessage;
    }
}
