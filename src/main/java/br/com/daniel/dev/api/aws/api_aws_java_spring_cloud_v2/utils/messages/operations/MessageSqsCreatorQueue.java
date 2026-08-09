package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages.operations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageSqsCreatorQueue {

    public void createQueueIfNeeded(String queueName) {
        log.info("Creating SQS queue: {}", queueName);
        // Implement the logic to create an SQS queue using AWS SDK
    }
}
