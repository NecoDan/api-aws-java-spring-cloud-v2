package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.secrets;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions.AwsSecretsManagerAccessException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions.IllegalAccessAwsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.*;

/**
 * Classe responsável por gerenciar operações com o AWS Secrets Manager.
 * Permite criar, recuperar e listar segredos armazenados no AWS Secrets Manager.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SecretsManagerCreator {

    private final SecretsManagerClient secretsManagerClient;

    /**
     * Cria um segredo no AWS Secrets Manager, caso ele ainda não exista.
     *
     * @param secretName  Nome do segredo a ser criado.
     * @param secretValue Valor do segredo.
     * @param description Descrição do segredo.
     * @return Resposta contendo os detalhes do segredo criado ou existente.
     * @throws AwsSecretsManagerAccessException Se ocorrer um erro ao acessar o Secrets Manager.
     * @throws IllegalAccessAwsException        Se ocorrer um erro inesperado ao verificar ou criar o segredo.
     */
    public GetSecretValueResponse createSecretsFromAWS(String secretName,
                                                       String secretValue,
                                                       String description) {
        log.info("Creating secrets manager if not exists: {}", secretName);

        try {
            return getSecretsFromAWS(secretName);
        } catch (SecretsManagerException e) {
            if (HttpStatus.valueOf(e.statusCode()) == HttpStatus.NOT_FOUND ||
                    HttpStatus.valueOf(e.statusCode()) == HttpStatus.BAD_REQUEST) {
                try {
                    secretsManagerClient.createSecret(CreateSecretRequest.builder()
                            .name(secretName)
                            .secretString(secretValue)
                            .description(description)
                            .build());

                    return getSecretsFromAWS(secretName);
                } catch (SecretsManagerException createException) {
                    log.error("Failed creating secrets manager {}: {}.", secretName, e.awsErrorDetails().errorMessage());
                    throw new AwsSecretsManagerAccessException(e.getMessage());
                }
            } else {
                System.err.printf("Error checking secrets manager existence %s: %s%n", secretName, e.awsErrorDetails().errorMessage());
                log.error("Error checking secrets manager existence {}: {}.", secretName, e.awsErrorDetails().errorMessage());
                throw new IllegalAccessAwsException(e.getMessage());
            }
        }
    }

    /**
     * Recupera os detalhes de um segredo armazenado no AWS Secrets Manager.
     *
     * @param secretName Nome do segredo a ser recuperado.
     * @return Resposta contendo os detalhes do segredo.
     * @throws AwsSecretsManagerAccessException Se o segredo não for encontrado ou ocorrer um erro ao acessá-lo.
     * @throws IllegalAccessAwsException        Se ocorrer um erro inesperado ao acessar o segredo.
     */
    public GetSecretValueResponse getSecretsFromAWS(String secretName) {
        log.info("Getting secrets manager: {}", secretName);

        try {
            final var secretValueResponse = secretsManagerClient.getSecretValue(buildGetSecretValueRequest(secretName));
            log.info("secrets manager already exists: {}", secretName);
            return secretValueResponse;
        } catch (SecretsManagerException e) {
            if (HttpStatus.valueOf(e.statusCode()) == HttpStatus.NOT_FOUND ||
                    HttpStatus.valueOf(e.statusCode()) == HttpStatus.BAD_REQUEST) {
                log.error("Failed getting secrets manager {}: {}.", secretName, e.awsErrorDetails().errorMessage());
                throw e;
            } else {
                System.err.printf("Error checking secrets manager existence %s: %s%n", secretName, e.awsErrorDetails().errorMessage());
                log.error("Error checking secrets manager existence {}: {}.", secretName, e.awsErrorDetails().errorMessage());
                throw new IllegalAccessAwsException(e.getMessage());
            }
        }
    }

    /**
     * Lista todos os segredos armazenados no AWS Secrets Manager.
     *
     * @return Resposta contendo a lista de todos os segredos.
     * @throws AwsSecretsManagerAccessException Se ocorrer um erro ao listar os segredos.
     */
    public ListSecretsResponse getAllSecretsFromAWS() {
        log.info("Getting all secrets manager");

        try {
            ListSecretsRequest request = ListSecretsRequest.builder().build();
            return secretsManagerClient.listSecrets(request);
        } catch (SecretsManagerException e) {
            log.error(e.getMessage());
            throw new AwsSecretsManagerAccessException(e.getMessage());
        }
    }

    private GetSecretValueRequest buildGetSecretValueRequest(String secretName) {
        return GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
    }
}
