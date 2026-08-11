package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages.operations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Component
@Slf4j
public class MessageSqsOperations extends MessageSqsTemplate {

    protected MessageSqsOperations(SqsClient sqsClient) {
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

    public void deleteMessage(String queueUrl,
                              String receiptHandle) {
        try {
            final var deleteRequest = DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(receiptHandle)
                    .build();

            System.out.println("Message processed and deleted successfully.");

            sqsClient.deleteMessage(deleteRequest);
            log.info("Message processed and deleted successfully.");
        } catch (SqsException e) {
            System.err.println("Error delete message from SQS: " + e.awsErrorDetails().errorMessage());
            log.error("Error delete message from SQS: {}.", e.awsErrorDetails().errorMessage());
        }
    }
}
