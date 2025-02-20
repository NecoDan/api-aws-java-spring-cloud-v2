package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.config;


import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions.IllegalAccessAwsException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.*;

import java.net.URI;
import java.util.Properties;

@Getter
@Component
@Slf4j
public class SecretsManagerConfig {

    private static final String LOCALSTACK_URL = "http://localhost:4566";

    private final SecretsManagerClient secretsManagerClient;

    SecretsManagerConfig() {

        this.secretsManagerClient = SecretsManagerClient.builder()
                .region(Region.SA_EAST_1)
                .endpointOverride(URI.create(LOCALSTACK_URL))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("****", "*****")
                ))
                .build();
    }

    public static Properties createSecretsFromAWS(SecretsManagerClient secretsManagerClient,
                                                  String secretName,
                                                  String secretValue,
                                                  String description) {
        try {
            secretsManagerClient.createSecret(CreateSecretRequest.builder()
                    .name(secretName)
                    .secretString(secretValue)
                    .description(description)
                    .build());

            return getSecretsFromAWS(secretsManagerClient, secretName);
        } catch (SecretsManagerException e) {
            log.error(e.getMessage());
            throw new IllegalAccessAwsException(e.getMessage());
        }
    }

    public static Properties getSecretsFromAWS(SecretsManagerClient secretsManagerClient, String secretName) {

        try {
            final GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();

            GetSecretValueResponse valueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
            Properties properties = new Properties();
            properties.setProperty(secretName, valueResponse.secretString());

            return properties;
        } catch (SecretsManagerException e) {
            log.error(e.getMessage());
            throw new IllegalAccessAwsException(e.getMessage());
        }
    }

    public static ListSecretsResponse getAllSecretsFromAWS(SecretsManagerClient secretsManagerClient) {
        try {
            ListSecretsRequest request = ListSecretsRequest.builder().build();
            return secretsManagerClient.listSecrets(request);
        } catch (SecretsManagerException e) {
            log.error(e.getMessage());
            throw new IllegalAccessAwsException(e.getMessage());
        }
    }
}
