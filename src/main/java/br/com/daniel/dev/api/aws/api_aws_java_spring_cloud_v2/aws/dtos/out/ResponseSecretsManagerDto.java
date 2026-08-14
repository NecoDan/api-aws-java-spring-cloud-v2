package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.dtos.out;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ResponseSecretsManagerDto(
        String secretName,
        String secretValue,
        String description,
        String arn
) {

    @JsonIgnore
    public static ResponseSecretsManagerDto from(String secretName) {
        return new ResponseSecretsManagerDto(secretName, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    @JsonIgnore
    public static ResponseSecretsManagerDto by(String secretName, String secretValue) {
        return new ResponseSecretsManagerDto(secretName, secretValue, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    @JsonIgnore
    public static ResponseSecretsManagerDto to(String secretName, String description, String arn) {
        return new ResponseSecretsManagerDto(secretName, StringUtils.EMPTY, description, arn);
    }
}
