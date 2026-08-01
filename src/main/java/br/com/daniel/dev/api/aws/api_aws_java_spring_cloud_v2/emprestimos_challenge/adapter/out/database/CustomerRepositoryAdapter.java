package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.database;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.entity.CustomerEntity;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.DynamoDbTemplateRepositoryImpl;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@Component
public class CustomerRepositoryAdapter extends DynamoDbTemplateRepositoryImpl<CustomerEntity> implements CustomerRepository {

    public CustomerRepositoryAdapter(DynamoDbEnhancedClient enhancedClient) {
        super(enhancedClient, "CustomerTable", CustomerEntity.class);
    }

    @Override
    public void save(CustomerEntity item) {
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
    public void delete(String partitionKey) {
        super.delete(partitionKey);
    }

    @Override
    public void delete(String partitionKey, String sortKey) {
        super.delete(partitionKey, sortKey);
    }

    @Override
    public void updateItem(CustomerEntity entity) {
        updateItem(entity);
    }
}
