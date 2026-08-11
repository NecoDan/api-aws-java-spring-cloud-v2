package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.database.CustomerDynamoAdapter;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.database.CustomerHistoryDynamoAdapter;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.exceptions.EmprestimoCustomerException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.Customer;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.CustomerHistory;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.port.CustomerPort;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.input.CustomerHistoryMovementInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerMovementRecordUseCaseImpl implements CustomerMovementRecordUseCase {

    private final CustomerHistoryDynamoAdapter customerHistoryDynamoAdapter;
    private final CustomerPort customerPort;

    @Override
    public void execute(CustomerHistoryMovementInput input) {
        try {
            log.info("Inicializando o fluxo de registro de movimento do cliente com a entrada: {}", input);

            final var customer = customerPort.buscarPorId(input.customerIdentifier())
                    .orElseThrow(() ->
                            new EmprestimoCustomerException("Cliente não encontrado para o identificador: %s"
                                    .formatted(input.customerIdentifier())
                            )
                    );

            log.info("Cliente encontrado com o ID: {}, para seguir com o fluxo.", customer.getId());
            customerHistoryDynamoAdapter.salvar(buildCustomerHistory(input));
            log.info("Fluxo de registro de movimento do cliente concluído com sucesso para o cliente.");
        } catch (Exception e) {
            log.error("Falhar ao processar o registro de movimento do histórico do cliente: {}. Detalhes do erro: {}",
                    input.customerIdentifier(),
                    e.getMessage(), e
            );

            throw new EmprestimoCustomerException("Falhar ao processar o registro de movimento do histórico do cliente: "
                    + input.customerIdentifier()
                    + ". Detalhes do erro: "
                    + e.getMessage(), e
            );
        }
    }

    private CustomerHistory buildCustomerHistory(CustomerHistoryMovementInput input) {
        return CustomerHistory.builder()
                .id(input.customerIdentifier())
                .conteudoEmprestimos(input.toJsonString())
                .build()
                .gerarDtAtualizacao();
    }
}
