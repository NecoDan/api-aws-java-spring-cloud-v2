package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.controller;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.FunctionalUtils;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages.operations.MessageSqsOperations;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SqsController {

    private final MessageSqsOperations sqsOperations;

    @PostMapping(value = "/v1/configs/sqs/payload_text_from_jsonbody", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTextStringFromMessageBody(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(FunctionalUtils.toStringJsonFrom(payload));
    }

    @PostMapping("/v1/configs/sqs/publish_message")
    public ResponseEntity<String> postPublishMessage(
            @Valid @RequestHeader("queue_name") String queueName,
            @Valid @RequestBody Map<String, Object> bodyMessagePayload
    ) {
        final var bodyMessage = FunctionalUtils.toStringJsonFrom(bodyMessagePayload);
        System.out.println(bodyMessage);

        sqsOperations.sendMessage(queueName, bodyMessage);
        return ResponseEntity.ok()
                .body("Mensagem enviada com sucesso para fila SQS: %s".formatted(queueName));
    }
}
