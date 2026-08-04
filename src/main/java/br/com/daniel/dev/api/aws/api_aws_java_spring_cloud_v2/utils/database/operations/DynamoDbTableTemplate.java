package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.operations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

@Component
@Slf4j
public abstract class DynamoDbTableTemplate {

    protected final DynamoDbClient dynamoDbClient;

    protected DynamoDbTableTemplate(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    protected DescribeTableRequest buildDescribeTableRequest(final String tableName) {
        return DescribeTableRequest.builder()
                .tableName(tableName)
                .build();
    }

    protected DeleteTableRequest buildDeleteTableRequest(final String tableName) {
        // 1. Criar e enviar a requisição DeleteTable
        return DeleteTableRequest.builder()
                .tableName(tableName)
                .build();
    }

    protected ScanRequest buildScanRequest(final String tableName) {
        return ScanRequest.builder()
                .tableName(tableName)
                .build();
    }

    protected boolean doesTableExist(String tableName) {
        try {
            this.dynamoDbClient.describeTable(buildDescribeTableRequest(tableName));
            return Boolean.TRUE;
        } catch (ResourceNotFoundException e) {
            return Boolean.FALSE;
        }
    }

}
