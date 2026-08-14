package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.config.advice;


import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions.AwsSecretsManagerAccessException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions.DataIntegrityViolationException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions.EntityCreateFailedException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity threatDuplicateEntity(DataIntegrityViolationException exception) {

        return ResponseEntity.badRequest()
                .body(ExceptionHandlerDTO.builder()
                        .message(String.format("Erro ao cadastrar usuário ou usuário já cadastrado: %s.", exception.getMessage()))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity threatArgumentNotValidExceptionBodyRequest(MethodArgumentNotValidException exception) {

        final var field = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getField();

        return ResponseEntity
                .badRequest()
                .body(ExceptionHandlerDTO.builder()
                        .message(String.format("Campo inválido e/ou inexistente (null): %s", field))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build()
                );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity threatNotFound(EntityNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ExceptionHandlerDTO.builder()
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build()
                );
    }

    @ExceptionHandler(EntityCreateFailedException.class)
    public ResponseEntity threatUnprocessableEntity(EntityCreateFailedException exception) {

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ExceptionHandlerDTO.builder()
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                        .build()
                );
    }

    @ExceptionHandler(AwsSecretsManagerAccessException.class)
    public ResponseEntity threatUnprocessableEntity(AwsSecretsManagerAccessException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ExceptionHandlerDTO.builder()
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build()
                );
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity threatGeneralRuntimeException(RuntimeException exception) {

        return ResponseEntity
                .internalServerError()
                .body(ExceptionHandlerDTO.builder()
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity threatGeneralException(Exception exception) {

        return ResponseEntity
                .internalServerError()
                .body(ExceptionHandlerDTO.builder()
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build()
                );
    }
}
