package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class BucketS3Config {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.dynamodb.endpoint}")
    private String endpoint;

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Bean
    public AwsCredentialsProvider s3CredentialsProvider() {
        return DefaultCredentialsProvider.builder()
                .asyncCredentialUpdateEnabled(true)
                .build();
    }

    @Bean
    public S3Client s3Client(@Qualifier("s3CredentialsProvider") AwsCredentialsProvider credentialsProvider) {
        var builder = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build()
                );

        if (endpoint != null && !endpoint.isBlank()) {
            builder.endpointOverride(URI.create(endpoint));
        }

        return builder.build();
    }
}
