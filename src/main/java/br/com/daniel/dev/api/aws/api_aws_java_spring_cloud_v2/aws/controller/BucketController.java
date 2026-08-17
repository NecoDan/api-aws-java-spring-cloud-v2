package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.controller;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.dtos.in.RequestBucketDto;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.dtos.out.ResponseBucketDto;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.dtos.out.ResponseSecretsManagerDto;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.FileUtils;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets.operations.BucketS3Creator;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets.operations.BucketS3Operations;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.logs.MdcUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Controlador responsável por gerenciar as operações relacionadas aos buckets no AWS S3.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class BucketController {

    private final BucketS3Operations bucketS3Operations;
    private final BucketS3Creator bucketS3Creator;

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
        try {
            MdcUtils.putTransactionIdRandom();

            final var listResponseBucketDto = bucketS3Operations.listBucketsAll()
                    .stream()
                    .map(bucket -> ResponseBucketDto.of(
                                    bucket.name(),
                                    bucket.bucketArn()
                            )
                    )
                    .toList();

            return ResponseEntity.ok().body(listResponseBucketDto);
        } finally {
            MdcUtils.clear();
        }
    }

    /**
     * Cria uma nova solicitação para criação de um bucket no serviço AWS S3.
     *
     * <p>O método recebe os dados do bucket por meio do corpo da requisição,
     * solicita sua criação caso ele ainda não exista e retorna os dados do
     * bucket criado.</p>
     *
     * @param payload dados da requisição contendo o nome do bucket a ser criado
     * @return {@link ResponseEntity} contendo os dados do bucket criado e o status HTTP {@code 201 (Created)}
     * @throws RuntimeException caso ocorra um erro durante a criação do bucket ou uma falha interna no processamento da requisição
     */
    @PostMapping("/v2/configs/buckets")
    @Operation(summary = "Cria um novo bucket no AWS S3 service.",
            description = "Cria uma nova solicitacao para criação de um bucket a partir do payload contido no corpo da requisição.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorna os dados com a nome bucket criado"),
            @ApiResponse(responseCode = "422", description = "Campos não atendem os requisitos para criação"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<ResponseBucketDto> postCreateBucket(
            @Valid @RequestBody RequestBucketDto payload
    ) {
        try {
            MdcUtils.putTransactionIdRandom();
            bucketS3Creator.createBucketIfNotExists(payload.bucketName());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ResponseBucketDto.from(payload.bucketName()));
        } finally {
            MdcUtils.clear();
        }
    }

    @GetMapping("/v2/configs/buckets/{bucket_name}")
    @Operation(summary = "Listar todos os recursos e/ou arquivos no buckets",
            description = "Listar o nome de dodos os recursos e/ou arquivos contidos no bucket via nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna os dados com os nomes e ARN do(s) recurso(s) existente(s)"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<ResponseBucketDto>> getAllFileInBuckets(@PathVariable("bucket_name") String bucketName) {
        try {
            MdcUtils.putTransactionIdRandom();

            final var listResponseBucketDto = bucketS3Operations.listObjectsInBucketFrom(bucketName)
                    .stream()
                    .map(s3Object -> ResponseBucketDto.by(s3Object.key(),
                                    s3Object.size(),
                                    Objects.isNull(s3Object.owner()) ? StringUtils.EMPTY : s3Object.owner().displayName(),
                                    Objects.isNull(s3Object.lastModified()) ? StringUtils.EMPTY : s3Object.lastModified().toString()
                            )
                    )
                    .toList();

            return ResponseEntity.ok().body(listResponseBucketDto);
        } finally {
            MdcUtils.clear();
        }
    }

    @GetMapping("/v2/configs/buckets/download")
    @Operation(summary = "Download a file from an S3 bucket.",
            description = "Downloads a file from the specified S3 bucket using the bucket name and file key.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Download do arquivo com sucesso."),
            @ApiResponse(responseCode = "404", description = "Arquivo não encontrado no bucket."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<byte[]> downloadFileFromBucket(@RequestParam("bucketName") String bucketName,
                                                         @RequestParam("fileNameKey") String fileNameKey) {
        try {
            MdcUtils.putTransactionIdRandom();

            final var pathNameFolderResources = FileUtils.getDefaultResourcesFolderPath().toAbsolutePath().toString();
            Path path = FileUtils.createFolderIfNotExists(pathNameFolderResources, "download-file-bucket");

            bucketS3Operations.downloadFile(bucketName, fileNameKey, path);
            byte[] fileData = Files.readAllBytes(path);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileNameKey + "\"")
                    .body(fileData);
        } catch (
                Exception e) {
            log.error("Error downloading file from bucket {}: {}", bucketName, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
