package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.database;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.entity.CustomerEntity;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.DynamoDbConstants;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.DynamoDbTemplateRepositoryImpl;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import java.util.Optional;

@Component
public class CustomerRepositoryAdapter extends DynamoDbTemplateRepositoryImpl<CustomerEntity> implements CustomerRepository {

    public CustomerRepositoryAdapter(DynamoDbEnhancedClient enhancedClient) {
        super(enhancedClient, DynamoDbConstants.TABLE_NAME_EMPRESTIMOS_CLIENTES, CustomerEntity.class);
    }

    @Override
    public Optional<CustomerEntity> buscarPorNumDocumentoCpf(String numCPF) {
        return super.findByAttribute("num_documento_cpf", numCPF).stream().findFirst();
    }
}
