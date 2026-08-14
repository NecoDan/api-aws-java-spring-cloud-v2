package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.dtos.out;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ResponseBucketDto(
        String name,
        String arn
) {

    @JsonIgnore
    public static ResponseBucketDto from(String name) {
        return new ResponseBucketDto(name, StringUtils.EMPTY);
    }

}

