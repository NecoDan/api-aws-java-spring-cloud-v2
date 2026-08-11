package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.controller;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.FunctionalUtils;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages.operations.MessageSqsSendProducer;
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

    private final MessageSqsSendProducer messageSqsSendProducer;

    @PostMapping(value = "/sqs/v1/payload_text_from_jsonbody", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTextStringFromMessageBody(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(FunctionalUtils.toStringJsonFrom(payload));
    }

    @PostMapping("/sqs/v1/publish_message")
    public ResponseEntity<String> postPublishMessage(
            @RequestHeader("queue_name") String queueName,
            @RequestBody Map<String, Object> bodyMessagePayload
    ) {
        final var bodyMessage = FunctionalUtils.toStringJsonFrom(bodyMessagePayload);
        System.out.println(bodyMessage);

        messageSqsSendProducer.sendMessage(queueName, bodyMessage);
        return ResponseEntity.ok().body("Mensagem enviada com sucesso para fila SQS: %s".formatted(queueName));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ApiResponse<T> {
        private String type;
        private T payload;
    }
}
