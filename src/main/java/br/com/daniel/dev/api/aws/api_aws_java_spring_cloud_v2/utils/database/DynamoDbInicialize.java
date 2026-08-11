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

        dynamoDbDropTable.dropTableIfNeeded(DynamoDbConstants.TABLE_NAME_HISTORICO_CLIENTE);
        dynamoDbCreatorTable.createTableIfNeeded(createDbTableHistoricoMovimentosCliente());
        dynamoDbReadItemsTable.scallAllItems(DynamoDbConstants.TABLE_NAME_HISTORICO_CLIENTE);
    }

    public DynamoDbTableTemplateModel createDbTableHistoricoMovimentosCliente() {
        final var mapAttributeDefitions = new HashMap<String, String>();
        mapAttributeDefitions.put(DynamoDbConstants.DEFAUL_KEY_CLIENTE_CODIGO, "S");
        mapAttributeDefitions.put(DynamoDbConstants.DEFAUL_KEY_DATA_CRIACAO, "S");

        return DynamoDbTableTemplateModel.builder()
                .tableName(DynamoDbConstants.TABLE_NAME_EMPRESTIMOS_CLIENTES)
                .attributeNameKeyHash(DynamoDbConstants.DEFAUL_KEY_CLIENTE_CODIGO)
                .mapAttributeDefinitions(mapAttributeDefitions)
                .templateGlobalSecondaryIndex(DynamoDbTableTemplateModel.TemplateGlobalSecondaryIndex.builder()
                        .valueIndexName("DtCriacaoIndex")
                        .attributeNameKeyRange(DynamoDbConstants.DEFAUL_KEY_DATA_CRIACAO)
                        .build()
                )
                .projectionType(ProjectionType.ALL)
                .readCapacityUnits(5L)
                .writeCapacityUnits(5L)
                .build();
    }

    public DynamoDbTableTemplateModel createDbTableEmprestimosCliente() {
        final var mapAttributeDefitions = new HashMap<String, String>();
        mapAttributeDefitions.put(DynamoDbConstants.DEFAUL_KEY_CLIENTE_CODIGO, "S");
        mapAttributeDefitions.put(DynamoDbConstants.DEFAUL_KEY_DATA_CRIACAO, "S");
        mapAttributeDefitions.put("num_documento_cpf", "S");

        return DynamoDbTableTemplateModel.builder()
                .tableName(DynamoDbConstants.TABLE_NAME_EMPRESTIMOS_CLIENTES)
                .attributeNameKeyHash(DynamoDbConstants.DEFAUL_KEY_CLIENTE_CODIGO)
                .mapAttributeDefinitions(mapAttributeDefitions)
                .templateGlobalSecondaryIndex(DynamoDbTableTemplateModel.TemplateGlobalSecondaryIndex.builder()
                        .valueIndexName("NumDocCpfIndex")
                        .attributeNameKeyHash("num_documento_cpf")
                        .attributeNameKeyRange(DynamoDbConstants.DEFAUL_KEY_DATA_CRIACAO)
                        .build()
                )
                .projectionType(ProjectionType.ALL)
                .readCapacityUnits(5L)
                .writeCapacityUnits(5L)
                .build();
    }
}
