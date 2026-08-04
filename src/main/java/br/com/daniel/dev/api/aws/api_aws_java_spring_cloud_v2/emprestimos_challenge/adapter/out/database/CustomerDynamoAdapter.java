package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.database;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.mappers.CustomerMapper;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.exceptions.EmprestimoCustomerException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.Customer;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.port.CustomerPort;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.DynamoDbConstants;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database.operations.DynamoDbReadItemsTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerDynamoAdapter implements CustomerPort {

    private final CustomerRepositoryAdapter customerRepositoryAdapter;
    private final CustomerMapper customerMapper;
    private final DynamoDbReadItemsTable dynamoDbReadItemsTable;

    @Override
    public void salvar(Customer customer) {
        try {
            log.info("Salvando o cliente: {}", customer.getName());
            var customerEntity = customerMapper.toEntity(customer);

            customerRepositoryAdapter.save(customerEntity);
            log.info("Cliente salvo com sucesso: {}", customer.getName());
        } catch (Exception e) {
            log.error("Erro ao salvar o cliente: {}. Detalhes do erro: {}", customer.getName(), e.getMessage(), e);
            throw new EmprestimoCustomerException("Erro ao salvar o cliente: " + customer.getName() + ". Detalhes do erro: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Customer customer) {
        try {
            log.info("Atualizando o cliente: {}", customer.toStringIdAndName());
            var customerEntity = customerMapper.toEntity(customer);

            customerRepositoryAdapter.updateItem(customerEntity);
            dynamoDbReadItemsTable.scallAllItems(DynamoDbConstants.TABLE_NAME_EMPRESTIMOS_CLIENTES);
            log.info("Cliente atualizado com sucesso: {}", customer.toStringIdAndName());
        } catch (Exception e) {
            log.error("Erro ao atualizar o cliente: {}. Detalhes do erro: {}", customer.getName(), e.getMessage(), e);
            throw new EmprestimoCustomerException("Erro ao atualizar o cliente: " + customer.getName() + ". Detalhes do erro: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Customer> buscarPorId(String id) {
        try {
            final var entity = customerRepositoryAdapter.getById(id);

            if (entity == null) {
                log.info("Cliente não encontrado para o ID: {}", id);
                return Optional.empty();
            }

            var customer = customerMapper.toCustomer(entity);
            log.info("Cliente encontrado: {}", customer);
            return Optional.of(customer);
        } catch (ResourceNotFoundException e) {
            log.error("Cliente não encontrado para o ID: {}. Detalhes do erro: {}", id, e.getMessage(), e);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Erro ao tentar recuperar o cliente para o ID: {}. Detalhes do erro: {}", id, e.getMessage(), e);
            throw new EmprestimoCustomerException("Erro ao tentar recuperar o cliente para o ID: {}: " + id + ". Detalhes do erro: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Customer> buscarPorNumeroDocumentoCpf(final String cpf) {
        try {
            final var entity = customerRepositoryAdapter.buscarPorNumDocumentoCpf(cpf);

            if (entity.isEmpty()) {
                log.info("Cliente não encontrado para o CPF: {}", cpf);
                return Optional.empty();
            }

            var customer = customerMapper.toCustomer(entity.get());
            log.info("Cliente encontrado: {}", customer);
            return Optional.of(customer);
        } catch (Exception e) {
            log.error("Erro ao tentar recuperar o cliente para o CPF: {}. Detalhes do erro: {}", cpf, e.getMessage(), e);
            throw new EmprestimoCustomerException("Erro ao tentar recuperar o cliente para o CPF: {}: " + cpf + ". Detalhes do erro: " + e.getMessage(), e);
        }
    }
}
