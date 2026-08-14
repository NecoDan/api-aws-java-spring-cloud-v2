package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.controller;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.dtos.out.ResponseBucketDto;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets.operations.BucketS3Operations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador responsável por gerenciar as operações relacionadas aos buckets no AWS S3.
 */
@RestController
@RequiredArgsConstructor
public class BucketController {

    private final BucketS3Operations bucketS3Operations;

    /**
     * Endpoint para obter todos os buckets criados no AWS S3.
     *
     * @return ResponseEntity contendo uma lista de objetos ResponseBucketDto com o nome e ARN dos buckets.
     * <p>
     * Respostas possíveis:
     * - 200: Retorna os dados com o nome e ARN do(s) bucket(s) criado(s).
     * - 400: Requisição inválida.
     * - 500: Erro interno do servidor.
     */
    @GetMapping("/v2/configs/buckets")
    @Operation(summary = "Obter todos os buckets criados no AWS S3.",
            description = "Retorna uma lista de todos os buckets criados no AWS S3.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna os dados com o nome e ARN do(s) bucket(s) criado(s)"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<ResponseBucketDto>> getAllBuckets() {
        final var listResponseBucketDto = bucketS3Operations.listBucketsAll()
                .stream()
                .map(bucket -> new ResponseBucketDto(bucket.name(), bucket.bucketArn()))
                .toList();

        return ResponseEntity.ok().body(listResponseBucketDto);
    }
}
