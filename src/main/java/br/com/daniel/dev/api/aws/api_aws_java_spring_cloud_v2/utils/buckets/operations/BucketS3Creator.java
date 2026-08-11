package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets.operations;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets.BucketS3Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * Generic component to create and obtain AWS S3 buckets.
 */
@Component
@Slf4j
public class BucketS3Creator extends BucketS3Template {

    protected BucketS3Creator(S3Client s3Client) {
        super(s3Client);
    }

    public void createBucketIfNotExists(String bucketName) {
        try {
            log.info("Creating S3 bucket if not exists: {}", bucketName);

            s3Client.createBucket(buildRequest(bucketName));
            System.out.println("Bucket created successfully: " + bucketName);
        } catch (S3Exception e) {
            System.err.println("Error creating S3 bucket: " + e.awsErrorDetails().errorMessage());
            log.error("Failed creating S3 bucket {}: {}.", bucketName, e.awsErrorDetails().errorMessage());
            throw e;
        }
    }
}
