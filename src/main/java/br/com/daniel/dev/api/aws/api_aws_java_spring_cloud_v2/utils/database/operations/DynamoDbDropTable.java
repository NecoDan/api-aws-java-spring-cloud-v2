package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.operations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

@Component
@Slf4j
public class DynamoDbDropTable extends DynamoDbTableTemplate {

    protected DynamoDbDropTable(DynamoDbClient dynamoDbClient) {
        super(dynamoDbClient);
    }

    public void dropTableIfNeeded(final String tableName) {
        log.info("Initializing table deletion{} ... ", tableName);
        System.out.printf("Initializing table deletion %s...%n", tableName);

        if (!doesTableExist(tableName)) {
            System.out.println("Table '" + tableName + "' does not exist.");
            log.info("Table {} does not exist in DynamoDB AWS service.", tableName);
            return;
        }

        System.out.println("Deleting table '" + tableName + "'...");
        log.info("Deleting table {} ...", tableName);
        dynamoDbClient.deleteTable(buildDeleteTableRequest(tableName));

        // 2. Aguardar até que a tabela seja completamente removida do DynamoDB
        System.out.println("Waiting for table '" + tableName + "' to be deleted...");
        log.info("Waiting for table {} to be deleted...", tableName);

        try (DynamoDbWaiter waiter = dynamoDbClient.waiter()) {
            waiter.waitUntilTableNotExists(buildDescribeTableRequest(tableName));
            System.out.println("Table '" + tableName + "' has been successfully dropped.");
            log.info("Table {} has been successfully dropped in DynamoDB AWS service.", tableName);
        }
    }
}
