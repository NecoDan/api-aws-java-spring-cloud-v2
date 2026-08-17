package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.dtos.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestBucketDto(@NotBlank @NotNull @Size(max = 63) String bucketName) {
}
