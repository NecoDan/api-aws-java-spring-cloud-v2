package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.InicializeComponent;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages.operations.MessageSqsCreatorQueue;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages.operations.MessageSqsOperations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageSqsInicialize implements InicializeComponent {

    private final MessageSqsCreatorQueue sqsCreatorQueue;
    private final MessageSqsOperations sqsOperations;

    @Value("${fila.receiver.name.sqs-solicitar-regitro-movimento-cliente}")
    private String urlSqs;

    @Override
    public void inicialize() {
        log.info("Initializing SQS queue from URL: {}", urlSqs);
        final var sqsCreated = sqsCreatorQueue.createQueueIfNotExists(urlSqs);
        log.info("SQS queue from URL created: {}", sqsCreated);

        log.info("Initializing SQS queue read messages: {}", urlSqs);
        sqsOperations.pollPrintingMessages(urlSqs);
    }
}
