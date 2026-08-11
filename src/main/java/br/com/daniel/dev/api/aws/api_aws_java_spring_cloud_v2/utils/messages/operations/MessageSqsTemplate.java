package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages.operations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;

import java.util.Map;

@Component
@Slf4j
public abstract class MessageSqsTemplate {

    protected final SqsClient sqsClient;

    protected MessageSqsTemplate(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    protected GetQueueUrlRequest buildGetQueueUrlRequest(String queueName) {
        return GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build();
    }

    protected CreateQueueRequest buildCreateQueueRequest(String queueName,
                                                         Map<String, String> attrs) {
        return CreateQueueRequest.builder()
                .queueName(queueName)
                .attributesWithStrings(attrs)
                .build();
    }

    protected GetQueueUrlResponse getQueueUrlResponse(GetQueueUrlRequest getQueueUrlRequest) {
        return sqsClient.getQueueUrl(getQueueUrlRequest);
    }

    protected GetQueueUrlResponse getQueueUrlResponseBy(String queueName) {
        return sqsClient.getQueueUrl(buildGetQueueUrlRequest(queueName));
    }
}
