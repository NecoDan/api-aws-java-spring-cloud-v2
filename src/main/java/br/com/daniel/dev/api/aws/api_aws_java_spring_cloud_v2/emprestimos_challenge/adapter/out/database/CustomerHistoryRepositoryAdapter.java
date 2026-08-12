package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.database;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.entity.CustomerHistoryEntity;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.DynamoDbConstants;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.DynamoDbTemplateRepositoryImpl;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import java.util.List;

@Component
public class CustomerHistoryRepositoryAdapter extends DynamoDbTemplateRepositoryImpl<CustomerHistoryEntity> implements CustomerHistoryRepository {

    public CustomerHistoryRepositoryAdapter(DynamoDbEnhancedClient enhancedClient) {
        super(enhancedClient, DynamoDbConstants.TABLE_NAME_HISTORICO_CLIENTE, CustomerHistoryEntity.class);
    }

    @Override
    public List<CustomerHistoryEntity> buscarMovimentosPor(final String identificadorCliente) {
        return super.findByAttribute("cod_idef_cli", identificadorCliente);
    }
}
