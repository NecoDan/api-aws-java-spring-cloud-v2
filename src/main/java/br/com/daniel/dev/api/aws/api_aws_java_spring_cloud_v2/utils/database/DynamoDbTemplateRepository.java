package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database;

import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.util.List;

public interface DynamoDbTemplateRepository<T> {

    void save(T item) throws Exception;

    T getById(String partitionKey) throws Exception;

    T getById(String partitionKey, String sortKey) throws ResourceNotFoundException, Exception;

    void delete(String partitionKey) throws Exception;

    void delete(String partitionKey, String sortKey) throws Exception;

    void updateItem(T entity) throws Exception;

    List<T> findByAttribute(String attributeName,
                            String attributeValue);

    //    <T> T getItem(Key key, Class<?> clazz);

    //    <T> T putItem(T entity);

    //    void deleteItem(Key key, Class<?> clazz);

    //    <T> void deleteItem(T entity);

    //    <T> PageIterable<T> scanAll(Class<?> clazz);

    //    <T> PageIterable<T> scanAll(Class<?> clazz, String indexName);

    //    Object getById(String partitionKey);

    //    Object getById(String partitionKey, String sortKey);
}
