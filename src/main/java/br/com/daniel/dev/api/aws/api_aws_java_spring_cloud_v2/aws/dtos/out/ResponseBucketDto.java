package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.dtos.out;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ResponseBucketDto(
        String name,
        String arn,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long size,
        String owner,
        String lastModified
) {

    @JsonIgnore
    public static ResponseBucketDto from(String name) {
        return new ResponseBucketDto(name,
                StringUtils.EMPTY,
                null,
                StringUtils.EMPTY,
                StringUtils.EMPTY
        );
    }

    @JsonIgnore
    public static ResponseBucketDto of(String name, String arn) {
        return new ResponseBucketDto(name,
                arn,
                null,
                StringUtils.EMPTY,
                StringUtils.EMPTY
        );
    }

    @JsonIgnore
    public static ResponseBucketDto by(String name,
                                       Long size,
                                       String owner,
                                       String lastModified) {
        return new ResponseBucketDto(name,
                StringUtils.EMPTY,
                size,
                owner,
                lastModified
        );
    }
}

