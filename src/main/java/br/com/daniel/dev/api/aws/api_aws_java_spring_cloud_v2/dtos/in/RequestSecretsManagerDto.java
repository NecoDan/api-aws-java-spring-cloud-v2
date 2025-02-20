package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.dtos.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestSecretsManagerDto implements Serializable {

    private String secretName;
    private String secretValue;
    private String description;
}
