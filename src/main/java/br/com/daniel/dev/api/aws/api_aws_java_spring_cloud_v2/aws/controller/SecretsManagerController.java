package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.controller;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.dtos.in.RequestSecretsManagerDto;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.dtos.out.ResponseSecretsManagerDto;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.config.SecretsConfig;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.logs.MdcUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SecretsManagerController {

    private final SecretsConfig secretsManagerConfig;

    @PostMapping("/secrets/v2/configuration/")
    public ResponseEntity<ResponseSecretsManagerDto> postCreateValueSecretsManager(@RequestBody RequestSecretsManagerDto body) {
        try {
            MdcUtils.putTransactionIdRandom();

            var propertiesSecrets = SecretsConfig.createSecretsFromAWS(
                    secretsManagerConfig.getSecretsManagerClient(), body.getSecretName(),
                    body.getSecretValue(), body.getDescription()
            );

            return ResponseEntity.ok()
                    .body(ResponseSecretsManagerDto.builder()
                            .secretName(propertiesSecrets.getProperty(body.getSecretName()))
                            .build()
                    );
        } finally {
            MdcUtils.clear();
        }
    }

    @GetMapping("/secrets/v2/configuration/{secret_name_id}")
    public ResponseEntity<ResponseSecretsManagerDto> getValueSecretsManagerById(@PathVariable("secret_name_id") String secretNameId) {
        try {
            MdcUtils.putTransactionIdRandom();

            var propertiesSecretsAwsLocalStack = SecretsConfig.getSecretsFromAWS(
                    secretsManagerConfig.getSecretsManagerClient(),
                    secretNameId
            );

            return ResponseEntity.ok()
                    .body(ResponseSecretsManagerDto.builder()
                            .secretName(secretNameId)
                            .secretValue(propertiesSecretsAwsLocalStack.getProperty(secretNameId))
                            .build()
                    );
        } finally {
            MdcUtils.clear();
        }
    }

    @GetMapping("/secrets/v2/configuration")
    public ResponseEntity<List<ResponseSecretsManagerDto>> getAllValuesSecretManager() {
        try {
            MdcUtils.putTransactionIdRandom();

            final var allSecretsFromAWS = SecretsConfig.getAllSecretsFromAWS(
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
        } finally {
            MdcUtils.clear();
        }
    }
}
