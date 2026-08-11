package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages.operations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Component
@Slf4j
public class MessageSqsDeleteItems extends MessageSqsTemplate {

    protected MessageSqsDeleteItems(SqsClient sqsClient) {
        super(sqsClient);
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
