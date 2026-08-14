package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.controller;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.dtos.in.RequestSecretsManagerDto;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.dtos.out.ResponseSecretsManagerDto;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.logs.MdcUtils;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.secrets.SecretsManagerCreator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controlador responsável por gerenciar operações relacionadas ao AWS Secrets Manager.
 */
@RestController
@RequiredArgsConstructor
public class SecretsManagerController {

    private final SecretsManagerCreator secretsManagerCreator;

    /**
     * Cria um novo segredo no AWS Secrets Manager.
     *
     * @param payload {RequestSecretsManagerDto} objeto contendo os dados do segredo a ser criado no corpo da requisição
     * @return ResponseEntity contendo os dados do segredo criado.
     */
    @PostMapping("/v2/configs/secrets")
    @Operation(summary = "Cria um novo segredo no AWS Secrets Manager.",
            description = "Cria uma nova solicitacao para criação do secrets manager a partir do payload contido no" +
                    "corpo da requisição contendo os dados do segredo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorna os dados com a nome do segredo criado"),
            @ApiResponse(responseCode = "422", description = "Campos não atendem os requisitos para criação"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<ResponseSecretsManagerDto> postCreateValueSecretsManager(
            @RequestBody RequestSecretsManagerDto payload
    ) {
        try {
            MdcUtils.putTransactionIdRandom();
            final var secretsFromAWS = secretsManagerCreator.createSecretsFromAWS(payload.secretName(),
                    payload.secretValue(),
                    payload.description()
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ResponseSecretsManagerDto.from(secretsFromAWS.name()));
        } finally {
            MdcUtils.clear();
        }
    }

    /**
     * Recupera um segredo específico do AWS Secrets Manager pelo ID.
     *
     * @param secretNameId {String} ID do segredo a ser recuperado.
     * @return ResponseEntity contendo os dados do segredo recuperado.
     */
    @GetMapping("/v2/configs/secrets/{secret_name_id}")
    public ResponseEntity<ResponseSecretsManagerDto> getValueSecretsManagerById(
            @PathVariable("secret_name_id") String secretNameId
    ) {
        try {
            MdcUtils.putTransactionIdRandom();

            var secretsFromAWS = secretsManagerCreator.getSecretsFromAWS(secretNameId);
            return ResponseEntity.ok().body(ResponseSecretsManagerDto.by(
                            secretsFromAWS.name(), secretsFromAWS.secretString()
                    )
            );
        } finally {
            MdcUtils.clear();
        }
    }

    /**
     * Recupera todos os segredos armazenados no AWS Secrets Manager.
     *
     * @return ResponseEntity {List<ResponseSecretsManagerDto>} contendo a lista de todos os segredos.
     */
    @GetMapping("/v2/configs/secrets")
    public ResponseEntity<List<ResponseSecretsManagerDto>> getAllValuesSecretManager() {
        try {
            MdcUtils.putTransactionIdRandom();

            final var allSecretsList = secretsManagerCreator.getAllSecretsFromAWS()
                    .secretList()
                    .stream()
                    .map(secretListEntry -> ResponseSecretsManagerDto.to(
                                    secretListEntry.name(),
                                    secretListEntry.description(),
                                    secretListEntry.arn()
                            )
                    )
                    .toList();

            return ResponseEntity.ok().body(allSecretsList);
        } finally {
            MdcUtils.clear();
        }
    }
}
