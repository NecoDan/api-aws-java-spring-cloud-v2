package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.operations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.ProjectionType;

import java.util.HashMap;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamoDbTableTemplateModel {

    private String tableName;
    private String attributeNameKeyHash;
    private String attributeNameKeyRange;
    private HashMap<String, String> mapAttributeDefinitions;
    private TemplateGlobalSecondaryIndex templateGlobalSecondaryIndex;
    private ProjectionType projectionType;
    private Long readCapacityUnits;
    private Long writeCapacityUnits;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateGlobalSecondaryIndex {
        private String valueIndexName;
        private String attributeNameKeyHash;
        private String attributeNameKeyRange;
    }
}
