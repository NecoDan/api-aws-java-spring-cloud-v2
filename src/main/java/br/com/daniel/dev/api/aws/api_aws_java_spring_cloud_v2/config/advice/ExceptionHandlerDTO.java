package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.config.advice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionHandlerDTO {

    @JsonProperty("mensagem")
    private String message;

    @JsonProperty("httpStatusCode")
    private String statusCode;

    @JsonIgnoreProperties
    private HttpStatus httpStatus;

    public String getStatusCode(){
        this.httpStatus = (Objects.isNull(this.httpStatus)) ?  HttpStatus.INTERNAL_SERVER_ERROR : this.httpStatus;
        return String.format("%s - %s", this.httpStatus.value(), this.httpStatus.getReasonPhrase());
    }
}
