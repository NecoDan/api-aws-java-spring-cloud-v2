package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database;

public interface DynamoDbTemplateRepository<T> {

    void save(T item);

    T getById(String partitionKey);

    T getById(String partitionKey, String sortKey);

    void delete(String partitionKey);

    void delete(String partitionKey, String sortKey);

    void updateItem(T entity);

    //    <T> T getItem(Key key, Class<?> clazz);

    //    <T> T putItem(T entity);

    //    void deleteItem(Key key, Class<?> clazz);

    //    <T> void deleteItem(T entity);

    //    <T> PageIterable<T> scanAll(Class<?> clazz);

    //    <T> PageIterable<T> scanAll(Class<?> clazz, String indexName);

    //    Object getById(String partitionKey);

    //    Object getById(String partitionKey, String sortKey);
}
