package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages.operations;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.QueueNameExistsException;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Generic component to create and obtain AWS SQS queues.
 */
@Component
@Slf4j
public class MessageSqsCreatorQueue extends MessageSqsTemplate {

    protected MessageSqsCreatorQueue(SqsClient sqsClient) {
        super(sqsClient);
    }

    /**
     * Cria uma nova fila com os atributos fornecidos.
     *
     * @param queueName  nome da fila (deve ser único na conta/região)
     * @param attributes mapa de atributos da fila (pode ser nulo ou vazio)
     */
    public void createQueueIfNeeded(String queueName,
                                    Map<String, String> attributes) {

        log.info("Initializing creating SQS queue: {}", queueName);
        System.out.printf("Initializing creating SQS queue: %s.%n", queueName);

        try {
            Map<String, String> attrs = attributes == null ? Collections.emptyMap() : attributes;
            log.info("Creating SQS queue: {} with attributes: {}.", queueName, attrs.keySet());
            System.out.printf("Creating SQS queue: %s with attributes: %s.%n", queueName, attrs.keySet());

            var createQueueRequest = buildCreateQueueRequest(queueName, attrs);
            var createQueueResponse = sqsClient.createQueue(createQueueRequest);

            log.info("Created SQS queue '{}' -> url={}", queueName, createQueueResponse.queueUrl());
            System.out.printf("Created SQS queue '%s' -> url=%s", queueName, createQueueResponse.queueUrl());
        } catch (QueueNameExistsException e) {
            // Se a fila já existir com exatamente os mesmos atributos, o SQS retorna a sua URL via `GetQueueUrl`.
            final var urlQueueExists = getQueueUrlResponseBy(queueName).queueUrl();
            log.error("Queue '{}' already exists. Fetching existing URL... {}.", queueName, urlQueueExists);
            System.out.printf("Queue '%s' already exists. Fetching existing URL... %s.", queueName, urlQueueExists);

        } catch (SqsException e) {
            final var erroMessage = e.awsErrorDetails() != null ? e.awsErrorDetails().errorMessage() : e.getMessage();
            System.out.printf("Failed to create SQS queue %s: %s.", queueName, erroMessage);

            log.error("Failed to create SQS queue {}: {}", queueName, erroMessage);
            throw e;
        }
    }

    /**
     * Obtém a URL da fila se ela existir, caso contrário, cria-a com os atributos fornecidos.
     *
     * @param queueName  nome da fila
     * @param attributes atributos a serem usados ao criar a fila caso ela não exista
     */
    public String createQueueIfNotExists(String queueName,
                                         Map<String, String> attributes) {
        try {
            final var getQueueUrlResponse = getQueueUrlResponseBy(queueName);
            final var urlSqs = getQueueUrlResponse.queueUrl();

            log.info("SQS queue '{}' already exists -> url={}", queueName, urlSqs);
            return urlSqs;
        } catch (QueueNameExistsException e) {
            log.info("Failed to get or create SQS queue {}: {}", queueName, e.getMessage());
            System.out.println("Queue '" + queueName + "' already exists. Fetching existing URL...");

        } catch (SqsException e) {
            // Se a fila não existir, a AWS retorna um erro com o código `NonExistentQueue`.
            var code = Objects.nonNull(e.awsErrorDetails()) ? e.awsErrorDetails().errorCode() : null;

            if ("AWS.SimpleQueueService.NonExistentQueue".equals(code) || Objects.isNull(code)) {
                // Deve tentar a criação da fila...
                createQueueIfNeeded(queueName, attributes);
            }

            log.error("Failed to get or create SQS queue {}: {}", queueName, e.getMessage());
            throw e;
        }

        return StringUtils.EMPTY;
    }

    /**
     * Conveniência: criar fila sem atributos parametrizados e definidos.
     */
    public void createQueueFrom(String queueName) {
        createQueueIfNeeded(queueName, Collections.emptyMap());
    }

    /**
     * Conveniência: criar caso não exista sem atributos parametrizados e definidos.
     */
    public String createQueueIfNotExists(String queueName) {
        return createQueueIfNotExists(queueName, Collections.emptyMap());
    }

}
