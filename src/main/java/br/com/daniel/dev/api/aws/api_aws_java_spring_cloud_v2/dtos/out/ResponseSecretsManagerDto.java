package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.dtos.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseSecretsManagerDto implements Serializable {

    @JsonProperty("secret_name")
    private String secretName;

    @JsonProperty("secret_value")
    private String secretValue;

    @JsonProperty("description")
    private String description;

    @JsonProperty("arn")
    private String arn;
}
