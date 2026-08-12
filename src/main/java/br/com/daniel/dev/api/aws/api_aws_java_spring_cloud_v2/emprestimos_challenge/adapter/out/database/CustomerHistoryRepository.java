package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.database;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.entity.CustomerHistoryEntity;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.DynamoDbTemplateRepository;

import java.util.List;

public interface CustomerHistoryRepository extends DynamoDbTemplateRepository<CustomerHistoryEntity> {
    List<CustomerHistoryEntity> buscarMovimentosPor(String identificadorCliente);
}
