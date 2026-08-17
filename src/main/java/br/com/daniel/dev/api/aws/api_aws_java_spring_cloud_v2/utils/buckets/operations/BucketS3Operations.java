package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets.operations;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions.AwsBucketS3AccessException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets.BucketS3Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Classe responsável por realizar operações em buckets S3 utilizando o cliente AWS SDK.
 */
@Component
@Slf4j
public class BucketS3Operations extends BucketS3Template {

    /**
     * @param s3Client Cliente S3 utilizado para realizar as operações.
     */
    protected BucketS3Operations(S3Client s3Client) {
        super(s3Client);
    }

    /**
     * Lista todos os buckets S3 disponíveis na conta.
     *
     * @return Lista de nomes dos buckets disponíveis.
     * @throws AwsBucketS3AccessException Se ocorrer algum erro ao acessar os buckets S3.
     */
    public List<String> listBucketsNames() {
        log.info("Listing all S3 buckets...");

        try {
            return s3Client.listBuckets()
                    .buckets()
                    .stream()
                    .map(Bucket::name)
                    .toList();
        } catch (Exception e) {
            System.err.printf("Error listing buckets: %sn", e.getMessage());
            log.error("Error listing buckets: {}", e.getMessage());
            throw new AwsBucketS3AccessException(e);
        }
    }

    /**
     * Lista todos os buckets S3 disponíveis na conta.
     *
     * @return Lista de objetos Bucket representando os buckets disponíveis.
     * @throws AwsBucketS3AccessException Se ocorrer algum erro ao acessar os buckets S3.
     */
    public List<Bucket> listBucketsAll() {
        log.info("Listing all S3 buckets...");

        try {
            return s3Client.listBuckets().buckets();
        } catch (Exception e) {
            System.err.printf("Error/failed listing buckets: %sn", e.getMessage());
            log.error("Error/failed listing buckets: {}", e.getMessage());
            throw new AwsBucketS3AccessException(e);
        }
    }

    /**
     * Exclui um bucket S3 existente. O bucket deve estar vazio antes de ser excluído.
     *
     * @param bucketName Nome do bucket a ser excluído.
     * @throws AwsBucketS3AccessException Se ocorrer algum erro ao excluir o bucket S3.
     */
    public void deleteBucket(String bucketName) {
        log.info("Deleting bucket: {}", bucketName);
        try {
            final var deleteBucketRequest = DeleteBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            s3Client.deleteBucket(deleteBucketRequest);

            System.out.println("Bucket deleted: " + bucketName);
            log.info("Bucket deleted: {}", bucketName);
        } catch (Exception e) {
            System.err.printf("Error deleting bucket: %sn", e.getMessage());
            log.error("Error deleting bucket: {}", e.getMessage());
            throw new AwsBucketS3AccessException(e);
        }
    }

    /**
     * Faz upload de um arquivo local para um bucket do S3.
     *
     * @param bucketName Nome do bucket onde o arquivo será armazenado.
     * @param keyName    Chave (nome do arquivo) no bucket.
     * @param filePath   Caminho do arquivo local a ser enviado.
     * @throws AwsBucketS3AccessException Se ocorrer algum erro ao fazer upload do arquivo para o bucket S3.
     */
    public void uploadFile(String bucketName,
                           String keyName,
                           Path filePath) {
        log.info("Uploading file to bucket: {}, key: {}, filePath: {}", bucketName, keyName, filePath);

        try {
            final var putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromFile(filePath)
            );

            System.out.println("File uploaded successfully to key: " + keyName);
            log.info("File uploaded successfully to key: {}.", keyName);
        } catch (Exception e) {
            System.err.printf("Error uploading file to bucket: %s, key: %s, filePath: %s. Error: %s%n",
                    bucketName, keyName, filePath, e.getMessage()
            );
            log.error("Error uploading file to bucket: {}, key: {}, filePath: {}. Error: {}",
                    bucketName, keyName, filePath, e.getMessage()
            );
            throw new AwsBucketS3AccessException(e);
        }
    }

    /**
     * Faz upload de um arquivo recebido como MultipartFile para um bucket do S3.
     *
     * @param bucketName Nome do bucket onde o arquivo será armazenado.
     * @param keyName    Chave (nome do arquivo) no bucket.
     * @param file       Arquivo recebido como MultipartFile.
     * @throws AwsBucketS3AccessException Se ocorrer algum erro ao fazer upload do arquivo para o bucket S3.
     */
    public void uploadFileBy(String bucketName,
                             String keyName,
                             MultipartFile file) {
        log.info("Uploading file to bucket: {}, key: {}, fileName: {}", bucketName, keyName, file.getOriginalFilename());

        try {
            final var putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            System.out.println("File uploaded successfully to key: " + keyName);
            log.info("File uploaded successfully to key: {}.", keyName);
        } catch (Exception e) {
            System.err.printf("Error uploading file to bucket: %s, key: %s, fileName: %s. Error: %s%n",
                    bucketName, keyName, file.getOriginalFilename(), e.getMessage()
            );
            log.error("Error uploading file to bucket: {}, key: {}, fileName: {}. Error: {}",
                    bucketName, keyName, file.getOriginalFilename(), e.getMessage()
            );
            throw new AwsBucketS3AccessException(e);
        }
    }

    /**
     * Faz download de um objeto do S3 para um caminho de arquivo local.
     *
     * @param bucketName      Nome do bucket onde o objeto está armazenado.
     * @param keyName         Chave (nome do arquivo) no bucket.
     * @param destinationPath Caminho local onde o arquivo será salvo.
     * @throws AwsBucketS3AccessException Se ocorrer algum erro ao fazer download do objeto do bucket S3.
     */
    public void downloadFile(String bucketName,
                             String keyName,
                             Path destinationPath) {
        try {
            log.info("Download file in bucket: {}. Key: {}.", bucketName, keyName);

            s3Client.getObject(GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(keyName)
                            .build(),
                    ResponseTransformer.toFile(destinationPath)
            );

            System.out.println("File downloaded successfully to: " + destinationPath);
            log.info("File downloaded successfully to: {}.", destinationPath);
        } catch (Exception e) {
            System.err.printf("Error downloading file from bucket: %s, key: %s, destinationPath: %s. Error: %s%n",
                    bucketName, keyName, destinationPath, e.getMessage()
            );
            log.error("Error downloading file from bucket: {}, key: {}, destinationPath: {}. Error: {}",
                    bucketName, keyName, destinationPath, e.getMessage()
            );
            throw new AwsBucketS3AccessException(e);
        }
    }

    /**
     * Lista todas as chaves de objetos em um bucket específico.
     *
     * @param bucketName Nome do bucket onde os objetos estão armazenados.
     * @return Lista de chaves dos objetos no bucket.
     * @throws AwsBucketS3AccessException Se ocorrer algum erro ao listar os objetos no bucket S3.
     */
    public List<String> listObjectsInBucket(String bucketName) {
        log.info("Listing objects in bucket: {}", bucketName);

        try {
            var listObjectsV2Response = s3Client.listObjectsV2(ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build()
            );

            return listObjectsV2Response.contents()
                    .stream()
                    .map(S3Object::key)
                    .toList();
        } catch (Exception e) {
            System.err.printf("Error listing objects in bucket: %s. Error: %s%n", bucketName, e.getMessage());
            log.error("Error listing objects in bucket: {}. Error: {}", bucketName, e.getMessage());
            throw new AwsBucketS3AccessException(e);
        }
    }

    /**
     * Lista todas as chaves de objetos em um bucket específico.
     *
     * @param bucketName Nome do bucket onde os objetos estão armazenados.
     * @return Lista de chaves dos objetos no bucket.
     * @throws AwsBucketS3AccessException Se ocorrer algum erro ao listar os objetos no bucket S3.
     */
    public List<S3Object> listObjectsInBucketFrom(String bucketName) {
        try {
            log.info("Listing objects in bucket: {}", bucketName);

            return s3Client.listObjectsV2(
                    ListObjectsV2Request.builder()
                            .bucket(bucketName)
                            .build()
            ).contents();
        } catch (Exception e) {
            System.err.printf("Error listing objects in bucket: %s. Error: %s%n", bucketName, e.getMessage());
            log.error("Error listing objects in bucket: {}. Error: {}", bucketName, e.getMessage());
            throw new AwsBucketS3AccessException(e);
        }
    }

    /**
     * Exclui um único arquivo/objeto de um bucket S3.
     *
     * @param bucketName Nome do bucket onde o objeto está armazenado.
     * @param keyName    Chave (nome do arquivo) do objeto a ser excluído.
     * @throws AwsBucketS3AccessException Se ocorrer algum erro ao excluir o objeto do bucket S3.
     */
    public void deleteObject(String bucketName, String keyName) {
        log.info("Deleting object from bucket: {}, key: {}", bucketName, keyName);
        try {
            final var deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            System.out.println("Object deleted: " + keyName);
            log.info("Object deleted: {}.", keyName);
        } catch (Exception e) {
            System.err.printf("Error deleting object from bucket: %s, key: %s. Error: %s%n", bucketName, keyName, e.getMessage());
            log.error("Error deleting object from bucket: {}, key: {}. Error: {}", bucketName, keyName, e.getMessage());
            throw new AwsBucketS3AccessException(e);
        }
    }
}
