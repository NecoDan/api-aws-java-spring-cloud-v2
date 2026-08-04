package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.database;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.entity.CustomerEntity;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.DynamoDbTemplateRepositoryImpl;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import java.util.Optional;

@Component
public class CustomerRepositoryAdapter extends DynamoDbTemplateRepositoryImpl<CustomerEntity> implements CustomerRepository {

    private static final String TABLE_NAME_EMPRESTIMOS_CLIENTES = "emprestimos_cliente";

    public CustomerRepositoryAdapter(DynamoDbEnhancedClient enhancedClient) {
        super(enhancedClient, TABLE_NAME_EMPRESTIMOS_CLIENTES, CustomerEntity.class);
    }

    @Override
    public void save(CustomerEntity item) throws Exception {
        super.save(item);
    }

    @Override
    public CustomerEntity getById(String partitionKey) {
        return getById(partitionKey);
    }

    @Override
    public CustomerEntity getById(String partitionKey, String sortKey) {
        return getById(partitionKey, sortKey);
    }

    @Override
    public void delete(String partitionKey) throws Exception {
        super.delete(partitionKey);
    }

    @Override
    public void delete(String partitionKey, String sortKey) throws Exception {
        super.delete(partitionKey, sortKey);
    }

    @Override
    public void updateItem(CustomerEntity entity) throws Exception {
        super.updateItem(entity);
    }

    @Override
    public Optional<CustomerEntity> buscarPorNumDocumentoCpf(String numCPF) {
        return super.findByAttribute("num_documento_cpf", numCPF).stream().findFirst();
    }
}
