package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database;

import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementação genérica de um repositório para operações com o DynamoDB usando o DynamoDbEnhancedClient.
 *
 * @param <T> O tipo da entidade que será manipulada pelo repositório.
 */
public class DynamoDbTemplateRepositoryImpl<T> implements DynamoDbTemplateRepository<T> {

    protected final DynamoDbTable<T> dynamoDbTable;

    /**
     * Construtor protegido para inicializar o repositório com o cliente DynamoDbEnhancedClient.
     *
     * @param enhancedClient Cliente DynamoDbEnhancedClient usado para interagir com o DynamoDB.
     * @param tableName Nome da tabela no DynamoDB.
     * @param clazz Classe da entidade associada à tabela.
     */
    protected DynamoDbTemplateRepositoryImpl(DynamoDbEnhancedClient enhancedClient,
                                             String tableName,
                                             Class<T> clazz) {
        this.dynamoDbTable = enhancedClient.table(tableName, TableSchema.fromBean(clazz));
    }

    /**
     * Obtém um item da tabela pelo valor da chave de partição.
     *
     * @param partitionKey Valor da chave de partição.
     * @return O item correspondente ou null se não encontrado.
     */
    @Override
    public T getById(String partitionKey) {
        return dynamoDbTable.getItem(r -> r.key(buildKeyFrom(partitionKey)));
    }

    /**
     * Obtém um item da tabela pelos valores da chave de partição e chave de ordenação.
     *
     * @param partitionKey Valor da chave de partição.
     * @param sortKey Valor da chave de ordenação.
     * @return O item correspondente ou null se não encontrado.
     */
    @Override
    public T getById(String partitionKey, String sortKey) {
        return dynamoDbTable.getItem(r -> r.key(buildKey(partitionKey, sortKey)));
    }

    /**
     * Salva um item na tabela.
     *
     * @param entity Entidade a ser salva.
     */
    @Override
    public void save(T entity) {
        dynamoDbTable.putItem(entity);
    }

    /**
     * Exclui um item da tabela pelo valor da chave de partição.
     *
     * @param partitionKey Valor da chave de partição.
     */
    @Override
    public void delete(String partitionKey) {
        dynamoDbTable.deleteItem(r -> r.key(buildKeyFrom(partitionKey)));
    }

    /**
     * Exclui um item da tabela pelos valores da chave de partição e chave de ordenação.
     *
     * @param partitionKey Valor da chave de partição.
     * @param sortKey Valor da chave de ordenação.
     */
    @Override
    public void delete(String partitionKey, String sortKey) {
        dynamoDbTable.deleteItem(r -> r.key(buildKey(partitionKey, sortKey)));
    }

    /**
     * Atualiza um item existente na tabela.
     *
     * @param entity Entidade a ser atualizada.
     */
    @Override
    public void updateItem(T entity) {
        dynamoDbTable.updateItem(entity);
    }

    /**
     * Busca itens na tabela com base em um atributo específico.
     *
     * @param attributeName Nome do atributo a ser filtrado.
     * @param attributeValue Valor do atributo a ser filtrado.
     * @return Lista de itens que correspondem ao filtro.
     */
    @Override
    public List<T> findByAttribute(String attributeName,
                                   String attributeValue) {
        List<T> results = new ArrayList<>();

        // Construir expressão para filtrar onde attributeName = :val
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

    private Key buildKey(String partitionKey, String sortKey) {
        return Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();
    }

    private Key buildKeyFrom(String partitionKey) {
        return Key.builder()
                .partitionValue(partitionKey)
                .build();
    }
}