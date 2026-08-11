package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages.operations;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
@Slf4j
public class MessageSqsSendProducer extends MessageSqsTemplate{

    protected MessageSqsSendProducer(SqsClient sqsClient) {
        super(sqsClient);
    }

    public void sendMessage(String queueUrl, String messageBody) {
        try {
            var sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .build();

            var sendMessageResponse = sqsClient.sendMessage(sendMessageRequest);
            log.info("Message sent successfully. Message ID: {}", sendMessageResponse.messageId());
            System.out.println("Message sent successfully. Message ID: " + sendMessageResponse.messageId());
        } catch (Exception e) {
            log.error("Error sending message to SQS: {}", e.getMessage());
            System.err.println("Error sending message to SQS: " + e.getMessage());
        }
    }
}
