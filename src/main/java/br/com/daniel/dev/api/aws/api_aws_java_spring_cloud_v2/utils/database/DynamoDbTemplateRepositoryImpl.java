package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Component
public class DynamoDbTemplateRepositoryImpl<T> implements DynamoDbTemplateRepository<T> {

    protected final DynamoDbTable<T> dynamoDbTable;

    protected DynamoDbTemplateRepositoryImpl(DynamoDbEnhancedClient enhancedClient,
                                             String tableName,
                                             Class<T> clazz) {
        this.dynamoDbTable = enhancedClient.table(tableName, TableSchema.fromBean(clazz));
    }

    @Override
    public T getById(String partitionKey) {
        final var key = Key.builder()
                .partitionValue(partitionKey)
                .build();

        return dynamoDbTable.getItem(r -> r.key(key));
    }

    @Override
    public T getById(String partitionKey, String sortKey) {
        Key key = Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();

        return dynamoDbTable.getItem(r -> r.key(key));
    }

    @Override
    public void save(T entity) {
        dynamoDbTable.putItem(entity);
    }

    @Override
    public void delete(String partitionKey) {
        final var key = Key.builder()
                .partitionValue(partitionKey)
                .build();

        dynamoDbTable.deleteItem(r -> r.key(key));
    }

    @Override
    public void delete(String partitionKey, String sortKey) {
        final var key = Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();

        dynamoDbTable.deleteItem(r -> r.key(key));
    }

    @Override
    public void updateItem(T entity) {
        dynamoDbTable.updateItem(entity);
    }
}
