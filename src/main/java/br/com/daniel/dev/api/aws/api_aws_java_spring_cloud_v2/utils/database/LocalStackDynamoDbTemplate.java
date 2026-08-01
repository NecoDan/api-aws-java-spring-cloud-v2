package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database;

import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

public interface LocalStackDynamoDbTemplate {

    <T> T getItem(Key key, Class<?> clazz);

    <T> T putItem(T entity);

    void deleteItem(Key key, Class<?> clazz);

    <T> void deleteItem(T entity);

    <T> T updateItem(T entity);

    <T> PageIterable<T> scanAll(Class<?> clazz);

    <T> PageIterable<T> scanAll(Class<?> clazz, String indexName);

}
