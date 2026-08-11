package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages.operations;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Component
@Slf4j
public class MessageSqsReadItems extends MessageSqsTemplate {

    protected MessageSqsReadItems(SqsClient sqsClient) {
        super(sqsClient);
    }

    public void pollPrintingMessages(String queueUrl) {
        try {
            var receiveRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(5)
                    .waitTimeSeconds(5)
                    .visibilityTimeout(30)
                    .build();

            final var receiveMessageResponse = sqsClient.receiveMessage(receiveRequest);
            final var messageList = receiveMessageResponse.messages();

            if (messageList.isEmpty()) {
                log.warn("No messages available in the queue.");
                System.out.println("No messages available in the queue.");
                return;
            }

            for (var message : messageList) {
                System.out.println("----------------------------------------");
                System.out.println("Message ID: " + message.messageId());
                System.out.println("Payload:    " + message.body());
            }
        } catch (SqsException e) {
            System.err.println("Error reading from SQS: " + e.awsErrorDetails().errorMessage());
            log.error("Error reading from SQS: {}.", e.awsErrorDetails().errorMessage());
        }
    }
}
