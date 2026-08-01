package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

@Component
public class LocalStackDynamoDbTemplateImpl implements LocalStackDynamoDbTemplate{

    private final


    public LocalStackDynamoDbTemplateImpl(LocalStackDynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @Override
    public <T> T getItem(Key key, Class<?> clazz) {
        return dynamoDbTemplate;
    }

    @Override
    public <T> T putItem(T entity) {
        return dynamoDbTemplate.;
    }

    @Override
    public void deleteItem(Key key, Class<?> clazz) {

    }

    @Override
    public <T> void deleteItem(T entity) {

    }

    @Override
    public <T> T updateItem(T entity) {
        return null;
    }

    @Override
    public <T> PageIterable<T> scanAll(Class<?> clazz) {
        return null;
    }

    @Override
    public <T> PageIterable<T> scanAll(Class<?> clazz, String indexName) {
        return null;
    }
}
