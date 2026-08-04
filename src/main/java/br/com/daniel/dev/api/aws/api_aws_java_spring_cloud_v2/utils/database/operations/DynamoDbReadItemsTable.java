package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.operations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DynamoDbReadItemsTable extends DynamoDbTableTemplate {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected DynamoDbReadItemsTable(DynamoDbClient dynamoDbClient) {
        super(dynamoDbClient);
    }

    public void scallAllItems(String tableName) {
        try {
            log.info("Initializing the search and reading of table {} items...", tableName);
            System.out.printf("Initializing the search and reading of table %s items...%n", tableName);

            if (!doesTableExist(tableName)) {
                System.out.println("Table '" + tableName + "' does not exist.");
                log.info("Table {} does not exist in DynamoDB AWS service.", tableName);
                return;
            }

            var scanResponse = dynamoDbClient.scan(buildScanRequest(tableName));
            System.out.println(convertScanResponseToJson(scanResponse));
        } catch (Exception e) {
            log.error("Error reading items from table {}: {}", tableName, e.getMessage(), e);
            throw new IllegalStateException("Error reading items from table " + tableName + ": " + e.getMessage(), e);
        }
    }

    private String convertScanResponseToJson(ScanResponse scanResponse) {
        // 1. Flatten the raw AWS AttributeValues into clean Java Maps
        List<Map<String, Object>> simplifiedItems = scanResponse.items()
                .stream()
                .map(this::extractItemAttributes)
                .toList();

        // 2. Converter a lista limpa em uma string JSON`
        return GSON.toJson(simplifiedItems);
    }

    private Map<String, Object> extractItemAttributes(Map<String, AttributeValue> item) {
        // Helper method to unwrap AWS AttributeValue types into native Java types
        Map<String, Object> plainMap = new HashMap<>();

        for (Map.Entry<String, AttributeValue> entry : item.entrySet()) {
            String key = entry.getKey();
            AttributeValue val = entry.getValue();

            // Extract based on the data type stored in DynamoDB
            if (val.s() != null) {
                plainMap.put(key, val.s()); // String
            } else if (val.n() != null) {
                plainMap.put(key, val.n()); // Number (kept as string or parse to double/long if preferred)
            } else if (val.bool() != null) {
                plainMap.put(key, val.bool()); // Boolean
            } else if (val.ss() != null && !val.ss().isEmpty()) {
                plainMap.put(key, val.ss()); // String Set
            }
            // Add more types (L, M, B, etc.) here if your table uses them
        }

        return plainMap;
    }
}
