package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets.operations;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets.BucketS3Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.Path;
import java.util.List;

@Component
@Slf4j
public class BucketS3Operations extends BucketS3Template {

    protected BucketS3Operations(S3Client s3Client) {
        super(s3Client);
    }

    /**
     * Listar todos os buckets S3 disponíveis na conta.
     */
    public List<String> listBuckets() {
        final var listBucketsResponse = s3Client.listBuckets();

        return listBucketsResponse.buckets()
                .stream()
                .map(Bucket::name)
                .toList();
    }

    /**
     * Excluir um bucket S3 existente (Ele deve estar vazio primeiro).
     */
    public void deleteBucket(String bucketName) {
        final var deleteBucketRequest = DeleteBucketRequest.builder()
                .bucket(bucketName)
                .build();

        s3Client.deleteBucket(deleteBucketRequest);
        System.out.println("Bucket deleted: " + bucketName);
    }

    /**
     * Object Operations (Files/Data)
     * Fazer upload de um arquivo local para um bucket do S3.
     */
    public void uploadFile(String bucketName, String keyName, Path filePath) {
        final var putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(filePath));
        System.out.println("File uploaded successfully to key: " + keyName);
    }

    /**
     * Object Operations (Files/Data)
     * Download objeto do S3 para um caminho de arquivo local.
     */
    public void downloadFile(String bucketName, String keyName, Path destinationPath) {
        final var getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        s3Client.getObject(getObjectRequest, ResponseTransformer.toFile(destinationPath));
        System.out.println("File downloaded successfully to: " + destinationPath);
    }

    /**
     * Object Operations (Files/Data)
     * Listar todas as chaves de objeto em um bucket específico.
     */
    public List<String> listObjectsInBucket(String bucketName) {
        final var listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        var listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);

        return listObjectsV2Response.contents().stream()
                .map(S3Object::key)
                .toList();
    }

    /**
     * Object Operations (Files/Data)
     * Excluir um único arquivo/objeto de um bucket S3.
     */
    public void deleteObject(String bucketName, String keyName) {
        final var deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        System.out.println("Object deleted: " + keyName);
    }
}
