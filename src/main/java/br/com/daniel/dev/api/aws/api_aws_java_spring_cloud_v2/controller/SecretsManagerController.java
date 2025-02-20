package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.controller;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.config.SecretsManagerConfig;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.dtos.in.RequestSecretsManagerDto;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.dtos.out.ResponseSecretsManagerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.secretsmanager.model.ListSecretsResponse;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SecretsManagerController {

    private final SecretsManagerConfig secretsManagerConfig;

    @PostMapping("/secrets/v2/configuration/")
    public ResponseEntity<ResponseSecretsManagerDto> postCreateValueSecretsManager(@RequestBody RequestSecretsManagerDto body) {
        Properties propertiesSecrets = SecretsManagerConfig.createSecretsFromAWS(
                secretsManagerConfig.getSecretsManagerClient(), body.getSecretName(),
                body.getSecretValue(), body.getDescription()
        );

        return ResponseEntity.ok().body(ResponseSecretsManagerDto.builder()
                        .secretName(propertiesSecrets.getProperty(body.getSecretName()))
                .build());
    }

    @GetMapping("/secrets/v2/configuration/{secret_name_id}")
    public ResponseEntity<ResponseSecretsManagerDto> getValueSecretsManagerById(@PathVariable("secret_name_id") String secretNameId) {

        Properties propertiesSecretsAwsLocalStack = SecretsManagerConfig.getSecretsFromAWS(
                secretsManagerConfig.getSecretsManagerClient(), secretNameId);

        return ResponseEntity.ok().body(ResponseSecretsManagerDto.builder()
                        .secretName(secretNameId)
                        .secretValue(propertiesSecretsAwsLocalStack.getProperty(secretNameId))
                .build());
    }

    @GetMapping("/secrets/v2/configuration")
    public ResponseEntity<List<ResponseSecretsManagerDto>> getAllValuesSecretManager(){

        final var allSecretsFromAWS = SecretsManagerConfig.getAllSecretsFromAWS(
                secretsManagerConfig.getSecretsManagerClient()
        );

        final var allSecretsList = allSecretsFromAWS.secretList()
                .stream()
                .map(secretListEntry -> ResponseSecretsManagerDto.builder()
                        .secretName(secretListEntry.name())
                        .description(secretListEntry.description())
                        .arn(secretListEntry.arn())
                        .build())
                .toList();

        return ResponseEntity.ok().body(allSecretsList);
    }
}
