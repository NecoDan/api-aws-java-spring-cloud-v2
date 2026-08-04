package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database;

import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamoDbTemplateRepositoryImpl<T> implements DynamoDbTemplateRepository<T> {

    protected final DynamoDbTable<T> dynamoDbTable;

    protected DynamoDbTemplateRepositoryImpl(DynamoDbEnhancedClient enhancedClient,
                                             String tableName,
                                             Class<T> clazz) {
        this.dynamoDbTable = enhancedClient.table(tableName, TableSchema.fromBean(clazz));
    }

    @Override
    public T getById(String partitionKey) throws Exception {
        final var key = Key.builder()
                .partitionValue(partitionKey)
                .build();

        return dynamoDbTable.getItem(r -> r.key(key));
    }

    @Override
    public T getById(String partitionKey, String sortKey) throws ResourceNotFoundException, Exception {
        Key key = Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();

        return dynamoDbTable.getItem(r -> r.key(key));
    }

    @Override
    public void save(T entity) throws Exception {
        dynamoDbTable.putItem(entity);
    }

    @Override
    public void delete(String partitionKey) throws Exception {
        final var key = Key.builder()
                .partitionValue(partitionKey)
                .build();

        dynamoDbTable.deleteItem(r -> r.key(key));
    }

    @Override
    public void delete(String partitionKey, String sortKey) throws Exception {
        final var key = Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();

        dynamoDbTable.deleteItem(r -> r.key(key));
    }

    @Override
    public void updateItem(T entity) throws Exception {
        dynamoDbTable.updateItem(entity);
    }

    @Override
    public List<T> findByAttribute(String attributeName,
                                   String attributeValue) {
        List<T> results = new ArrayList<>();

        // Construir expressão para filtrar onde attributeName = :val`
        var filterExpression = Expression.builder()
                .expression("#attr = :val")
                .expressionNames(Map.of("#attr", attributeName))
                .expressionValues(Map.of(":val", AttributeValue.fromS(attributeValue)))
                .build();

        var scanRequest = ScanEnhancedRequest.builder()
                .filterExpression(filterExpression)
                .build();

        dynamoDbTable.scan(scanRequest)
                .items()
                .forEach(results::add);

        return results;
    }
}
