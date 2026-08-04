package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.exceptions;

import org.springframework.lang.Nullable;

public class DataIntegrityViolationException extends RuntimeException {

    public DataIntegrityViolationException(@Nullable String message) {
        super(message);
    }

    public DataIntegrityViolationException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
