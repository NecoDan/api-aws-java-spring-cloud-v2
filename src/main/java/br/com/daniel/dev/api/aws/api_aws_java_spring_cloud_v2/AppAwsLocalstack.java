package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets.BucketS3Inicialize;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.DynamoDbInicialize;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages.MessageSqsInicialize;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class AppAwsLocalstack implements CommandLineRunner {

    private final DynamoDbInicialize dynamoDbInicialize;
    private final MessageSqsInicialize messageSqsInicialize;
    private final BucketS3Inicialize bucketS3Inicialize;

    public static void main(String[] args) {
        SpringApplication.run(AppAwsLocalstack.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        messageSqsInicialize.inicialize();
        dynamoDbInicialize.inicialize();
        bucketS3Inicialize.inicialize();
    }
}
