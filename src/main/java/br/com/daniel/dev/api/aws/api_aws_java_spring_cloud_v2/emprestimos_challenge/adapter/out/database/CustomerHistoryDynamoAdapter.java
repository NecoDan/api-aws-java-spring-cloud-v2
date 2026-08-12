package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.database;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.mappers.CustomerMapper;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.exceptions.EmprestimoCustomerException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.CustomerHistory;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.port.CustomerHistoryPort;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.DynamoDbConstants;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.operations.DynamoDbReadItemsTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerHistoryDynamoAdapter implements CustomerHistoryPort {

    private final CustomerHistoryRepositoryAdapter repositoryAdapter;
    private final DynamoDbReadItemsTable dynamoDbReadItemsTable;
    private final CustomerMapper customerMapper;

    @Override
    public void salvar(CustomerHistory customerHistory) {
        try {
            log.info("Salvando o histórico movimentação do cliente: {}", customerHistory);
            var customerHistoryEntity = customerMapper.toCustomerHistoryEntity(customerHistory);

            repositoryAdapter.save(customerHistoryEntity);
            dynamoDbReadItemsTable.scallAllItems(DynamoDbConstants.TABLE_NAME_HISTORICO_CLIENTE);

            log.info("Historio do cliente salvo com sucesso: {}", customerHistory);
        } catch (Exception e) {
            log.error("Erro ao salvar o historico movimentos do cliente: {}. Detalhes do erro: {}",
                    customerHistory.getId(),
                    e.getMessage(), e
            );

            throw new EmprestimoCustomerException("Erro ao salvar o historico de movimentos do cliente: "
                    + customerHistory.getId()
                    + ". Detalhes do erro: "
                    + e.getMessage(), e
            );
        }
    }

    @Override
    public List<CustomerHistory> buscarMovimentosPor(final String identificadorCliente) {
        log.info("Efetuar buscar de movimento(s) historico(s) do cliente por ID: {}", identificadorCliente);

        return repositoryAdapter.buscarMovimentosPor(identificadorCliente)
                .stream()
                .map(customerMapper::toCustomerHistory)
                .toList();
    }
}
