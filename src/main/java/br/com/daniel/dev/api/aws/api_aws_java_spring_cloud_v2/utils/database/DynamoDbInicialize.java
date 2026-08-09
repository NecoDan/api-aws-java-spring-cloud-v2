package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.InicializeComponent;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.operations.DynamoDbCreatorTable;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.operations.DynamoDbDropTable;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.operations.DynamoDbReadItemsTable;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.operations.DynamoDbTableTemplateModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.ProjectionType;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class DynamoDbInicialize implements InicializeComponent {

    private final DynamoDbCreatorTable dynamoDbCreatorTable;
    private final DynamoDbDropTable dynamoDbDropTable;
    private final DynamoDbReadItemsTable dynamoDbReadItemsTable;

    @Override
    public void inicialize() {
        dynamoDbDropTable.dropTableIfNeeded(DynamoDbConstants.TABLE_NAME_EMPRESTIMOS_CLIENTES);
        dynamoDbCreatorTable.createTableIfNeeded(createDbTableEmprestimosCliente());
        dynamoDbReadItemsTable.scallAllItems(DynamoDbConstants.TABLE_NAME_EMPRESTIMOS_CLIENTES);
    }

    public DynamoDbTableTemplateModel createDbTableEmprestimosCliente() {
        final var mapAttributeDefitions = new HashMap<String, String>();
        mapAttributeDefitions.put("cod_idef_cliente", "S");
        mapAttributeDefitions.put("data_criacao", "S");
        mapAttributeDefitions.put("num_documento_cpf", "S");

        return DynamoDbTableTemplateModel.builder()
                .tableName(DynamoDbConstants.TABLE_NAME_EMPRESTIMOS_CLIENTES)
                .attributeNameKeyHash("cod_idef_cliente")
                .mapAttributeDefinitions(mapAttributeDefitions)
                .templateGlobalSecondaryIndex(DynamoDbTableTemplateModel.TemplateGlobalSecondaryIndex.builder()
                        .valueIndexName("NumDocCpfIndex")
                        .attributeNameKeyHash("num_documento_cpf")
                        .attributeNameKeyRange("data_criacao")
                        .build()
                )
                .projectionType(ProjectionType.ALL)
                .readCapacityUnits(5L)
                .writeCapacityUnits(5L)
                .build();
    }
}
