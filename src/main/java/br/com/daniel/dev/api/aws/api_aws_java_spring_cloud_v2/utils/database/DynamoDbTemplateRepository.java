package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database;

import java.util.List;

public interface DynamoDbTemplateRepository<T> {

    /**
     * Salva um item na tabela.
     *
     * @param item Entidade a ser salva.
     */
    void save(T item);

    /**
     * Obtém um item da tabela pelo valor da chave de partição.
     *
     * @param partitionKey Valor da chave de partição.
     * @return O item correspondente ou null se não encontrado.
     */
    T getById(String partitionKey);

    /**
     * Obtém um item da tabela pelos valores da chave de partição e chave de ordenação.
     *
     * @param partitionKey Valor da chave de partição.
     * @param sortKey Valor da chave de ordenação.
     * @return O item correspondente ou null se não encontrado.
     */
    T getById(String partitionKey, String sortKey);

    /**
     * Exclui um item da tabela pelo valor da chave de partição.
     *
     * @param partitionKey Valor da chave de partição.
     */
    void delete(String partitionKey);

    /**
     * Exclui um item da tabela pelos valores da chave de partição e chave de ordenação.
     *
     * @param partitionKey Valor da chave de partição.
     * @param sortKey Valor da chave de ordenação.
     */
    void delete(String partitionKey, String sortKey);

    /**
     * Atualiza um item existente na tabela.
     *
     * @param entity Entidade a ser atualizada.
     */
    void updateItem(T entity);

    /**
     * Busca itens na tabela com base em um atributo específico.
     *
     * @param attributeName Nome do atributo a ser filtrado.
     * @param attributeValue Valor do atributo a ser filtrado.
     * @return Lista de itens que correspondem ao filtro.
     */
    List<T> findByAttribute(String attributeName,
                            String attributeValue);

    //    <T> T getItem(Key key, Class<?> clazz);

    //    <T> T putItem(T entity);

    //    void deleteItem(Key key, Class<?> clazz);

    //    <T> void deleteItem(T entity);

    //    <T> PageIterable<T> scanAll(Class<?> clazz);

    //    <T> PageIterable<T> scanAll(Class<?> clazz, String indexName);
}
