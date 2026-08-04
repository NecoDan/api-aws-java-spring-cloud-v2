package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.operations;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class DynamoDbCreatorTable extends DynamoDbTableTemplate {

    protected DynamoDbCreatorTable(DynamoDbClient dynamoDbClient) {
        super(dynamoDbClient);
    }

    public void createTableIfNeeded(final DynamoDbTableTemplateModel dbTableTemplate) {
        final var tableName = dbTableTemplate.getTableName();
        log.info("Initializing table creation {}...", tableName);
        System.out.printf("Initializing table creation %s...%n", tableName);

        if (doesTableExist(tableName)) {
            log.info("Table {} already exists.", tableName);
            System.out.println("Table '" + tableName + "' already exists.");
            return;
        }

        log.info("Creating table {} ... ", tableName);
        System.out.println("Creating table '" + tableName + "'...");

        final var createTableRequest = CreateTableRequest.builder()
                .tableName(tableName)
                // 1. Definir o esquema de chave da tabela principal (Chave de partição: year (N), Chave de classificação: title (S))`
                .keySchema(buildKeySchemaElementFrom(dbTableTemplate))
                // 2. Definir os tipos de atributo para todas as chaves usadas na tabela e nos GSIs`
                .attributeDefinitions(buildAttributeDefinitionFrom(dbTableTemplate.getMapAttributeDefinitions()))
                // 3. (Opcional) Definir um Índice Secundário Global (GSI)`
                .globalSecondaryIndexes(
                        GlobalSecondaryIndex.builder()
                                .indexName(dbTableTemplate.getTemplateGlobalSecondaryIndex().getValueIndexName())
                                .keySchema(
                                        KeySchemaElement.builder()
                                                .attributeName(dbTableTemplate.getTemplateGlobalSecondaryIndex().getAttributeNameKeyHash())
                                                .keyType(KeyType.HASH)
                                                .build(),
                                        KeySchemaElement.builder()
                                                .attributeName(dbTableTemplate.getTemplateGlobalSecondaryIndex().getAttributeNameKeyRange())
                                                .keyType(KeyType.RANGE)
                                                .build()
                                )
                                .projection(
                                        Projection.builder()
                                                .projectionType(dbTableTemplate.getProjectionType())
                                                .build()
                                )
                                .provisionedThroughput(ProvisionedThroughput.builder()
                                        .readCapacityUnits(dbTableTemplate.getReadCapacityUnits())
                                        .writeCapacityUnits(dbTableTemplate.getWriteCapacityUnits())
                                        .build()
                                )
                                .build()
                )
                // 4. Configuração de cobrança (Provisionado vs. Pague por requisição)`
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(dbTableTemplate.getReadCapacityUnits())
                        .writeCapacityUnits(dbTableTemplate.getWriteCapacityUnits())
                        .build()
                )
                .build();

        this.dynamoDbClient.createTable(createTableRequest);

        System.out.println("Waiting for table '" + tableName + "' to become active...");
        log.info("Waiting for table {} to become active...", tableName);

        // 5. Bloquear e aguardar até que o status da tabela mude para ACTIVE`
        try (DynamoDbWaiter waiter = this.dynamoDbClient.waiter()) {
            waiter.waitUntilTableExists(buildDescribeTableRequest(tableName));
            log.info("Table {} is now active and ready!", tableName);
            System.out.println("Table '" + tableName + "' is now active and ready!");
        }
    }

    private Collection<KeySchemaElement> buildKeySchemaElementFrom(final DynamoDbTableTemplateModel dbTableTemplate) {
        var keySchemaElementsList = new ArrayList<>(
                List.of(
                        KeySchemaElement.builder()
                                .attributeName(dbTableTemplate.getAttributeNameKeyHash())
                                .keyType(KeyType.HASH)
                                .build()
                ));

        if (StringUtils.isNotEmpty(dbTableTemplate.getAttributeNameKeyRange())) {
            keySchemaElementsList.add(KeySchemaElement.builder()
                    .attributeName(dbTableTemplate.getAttributeNameKeyRange())
                    .keyType(KeyType.RANGE)
                    .build()
            );
        }

        return keySchemaElementsList;

    }

    private Collection<AttributeDefinition> buildAttributeDefinitionFrom(HashMap<String, String> mapAttributeDefinitions) {
        return mapAttributeDefinitions.entrySet()
                .stream()
                .map(entry -> AttributeDefinition.builder()
                        .attributeName(entry.getKey())
                        .attributeType(ScalarAttributeType.fromValue(entry.getValue()))
                        .build()
                )
                .toList();
    }
}
