package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.config.advice;


import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions.AwsSecretsManagerAccessException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions.DataIntegrityViolationException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions.EntityCreateFailedException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;

import java.util.Objects;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> threatDuplicateEntity(DataIntegrityViolationException exception) {

        return ResponseEntity.badRequest()
                .body((ErrorResponse) ExceptionHandlerDTO.builder()
                        .message(String.format("Erro ao cadastrar usuário ou usuário já cadastrado: %s.", exception.getMessage()))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build()
                );
    }

    @ExceptionHandler(BucketAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleBucketAlreadyExists(BucketAlreadyExistsException ex) {

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body((ErrorResponse) ExceptionHandlerDTO.builder()
                        .message(String.format("Erro ao criar bucket ou bucket existente: %s.", ex.getMessage()))
                        .httpStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> threatArgumentNotValidExceptionBodyRequest(MethodArgumentNotValidException exception) {

        final var field = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getField();

        return ResponseEntity
                .badRequest()
                .body((ErrorResponse) ExceptionHandlerDTO.builder()
                        .message(String.format("Campo inválido e/ou inexistente (null): %s", field))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build()
                );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> threatNotFound(EntityNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body((ErrorResponse) ExceptionHandlerDTO.builder()
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build()
                );
    }

    @ExceptionHandler(EntityCreateFailedException.class)
    public ResponseEntity<ErrorResponse> threatUnprocessableEntity(EntityCreateFailedException exception) {

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body((ErrorResponse) ExceptionHandlerDTO.builder()
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                        .build()
                );
    }

    @ExceptionHandler(AwsSecretsManagerAccessException.class)
    public ResponseEntity<ErrorResponse> threatUnprocessableEntity(AwsSecretsManagerAccessException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body((ErrorResponse) ExceptionHandlerDTO.builder()
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build()
                );
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> threatGeneralRuntimeException(RuntimeException exception) {

        return ResponseEntity
                .internalServerError()
                .body((ErrorResponse) ExceptionHandlerDTO.builder()
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> threatGeneralException(Exception exception) {

        return ResponseEntity
                .internalServerError()
                .body((ErrorResponse) ExceptionHandlerDTO.builder()
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build()
                );
    }
}
