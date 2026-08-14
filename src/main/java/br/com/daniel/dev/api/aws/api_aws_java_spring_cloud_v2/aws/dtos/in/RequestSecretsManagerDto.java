package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.dtos.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestSecretsManagerDto(@NotBlank @NotNull String secretName,
                                       @NotBlank @NotNull String secretValue,
                                       @NotBlank @NotNull String description
) {
}
