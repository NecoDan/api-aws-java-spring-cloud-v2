package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets.operations;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets.BucketS3Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;


/**
 * Component responsible for creating S3 buckets using the AWS SDK.
 * Extends the {@link BucketS3Template} to reuse common S3 operations.
 */
@Component
@Slf4j
public class BucketS3Creator extends BucketS3Template {

    /**
     * Constructor for BucketS3Creator.
     *
     * @param s3Client The {@link S3Client} instance used to interact with AWS S3.
     */
    protected BucketS3Creator(S3Client s3Client) {
        super(s3Client);
    }

    /**
     * Creates an S3 bucket if it does not already exist.
     *
     * @param bucketName o nome do bucket a ser criado.
     * @throws S3Exception If an error occurs during the bucket creation process.
     */
    public void createBucketIfNotExists(String bucketName) {
        log.info("Creating S3 bucket if not exists: {}", bucketName);

        try {
            s3Client.headBucket(builder -> builder.bucket(bucketName));
            log.info("Bucket already exists: {}", bucketName);
        } catch (S3Exception e) {
            if (HttpStatus.valueOf(e.statusCode()) == HttpStatus.NOT_FOUND) {
                try {
                    log.info("Creating S3 bucket: {}", bucketName);
                    s3Client.createBucket(buildRequest(bucketName));

                    log.info("Bucket created successfully: {}", bucketName);
                } catch (S3Exception createException) {
                    System.err.println("Error creating S3 bucket: " + e.awsErrorDetails().errorMessage());
                    log.error("Failed creating S3 bucket {}: {}.", bucketName, e.awsErrorDetails().errorMessage());
                    throw createException;
                }
            } else {
                System.err.printf("Error checking bucket existence %s: %s%n", bucketName, e.awsErrorDetails().errorMessage());
                log.error("Error checking bucket existence {}: {}.", bucketName, e.awsErrorDetails().errorMessage());
                throw e;
            }
        }
    }
}
