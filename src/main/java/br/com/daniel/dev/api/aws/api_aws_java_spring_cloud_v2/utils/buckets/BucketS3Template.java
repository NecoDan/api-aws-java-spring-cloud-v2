package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

@Component
@Slf4j
public abstract class BucketS3Template {

    protected final S3Client s3Client;

    protected BucketS3Template(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    protected CreateBucketRequest buildRequest(String bucketName) {
        return CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();
    }
}
