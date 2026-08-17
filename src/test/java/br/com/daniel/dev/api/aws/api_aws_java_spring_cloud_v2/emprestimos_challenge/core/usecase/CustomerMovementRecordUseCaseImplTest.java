package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.database.CustomerHistoryDynamoAdapter;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.enums.LoanType;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.exceptions.EmprestimoCustomerException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.Customer;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.CustomerHistory;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.port.CustomerPort;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.input.CustomerHistoryMovementInput;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.output.LoanOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerMovementRecordUseCaseImplTest {

    @Mock
    private CustomerHistoryDynamoAdapter customerHistoryDynamoAdapter;

    @Mock
    private CustomerPort customerPort;

    @InjectMocks
    private CustomerMovementRecordUseCaseImpl customerMovementRecordUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldPersistCustomerHistoryWhenCustomerExists() {
        var customer = Customer.builder()
                .name("Maria Souza")
                .build();

        var input = new CustomerHistoryMovementInput(
                "customer-123",
                List.of(new LoanOutput(LoanType.PERSONAL, 2.9D))
        );

        when(customerPort.buscarPorId("customer-123")).thenReturn(Optional.of(customer));

        customerMovementRecordUseCase.execute(input);

        var customerHistoryCaptor = ArgumentCaptor.forClass(CustomerHistory.class);
        verify(customerHistoryDynamoAdapter).salvar(customerHistoryCaptor.capture());

        var savedHistory = customerHistoryCaptor.getValue();
        assertNotNull(savedHistory.getId());
        assertEquals(input.toJsonString(), savedHistory.getConteudoEmprestimos());
        assertNotNull(savedHistory.getDtUltimaAtualizacao());
    }

    @Test
    void shouldThrowExceptionWhenCustomerIsNotFound() {
        var input = new CustomerHistoryMovementInput(
                "missing-customer",
                List.of(new LoanOutput(LoanType.CONSIGNMENT, 1.7D))
        );

        when(customerPort.buscarPorId("missing-customer")).thenReturn(Optional.empty());

        var exception = assertThrows(EmprestimoCustomerException.class, () -> customerMovementRecordUseCase.execute(input));

        assertTrue(exception.getMessage().contains("Cliente não encontrado"));
        verify(customerHistoryDynamoAdapter, never()).salvar(any(CustomerHistory.class));
    }

    @Test
    void shouldWrapPersistenceFailuresInCustomerException() {
        var customer = Customer.builder()
                .id("customer-456")
                .name("Joao")
                .build();
        var input = new CustomerHistoryMovementInput(
                "customer-456",
                List.of(new LoanOutput(LoanType.GUARANTEED, 3.5D))
        );

        when(customerPort.buscarPorId("customer-456")).thenReturn(Optional.of(customer));
        doThrow(new RuntimeException("dynamo unavailable")).when(customerHistoryDynamoAdapter).salvar(any(CustomerHistory.class));

        var exception = assertThrows(EmprestimoCustomerException.class, () -> customerMovementRecordUseCase.execute(input));

        assertTrue(exception.getMessage().contains("Falhar ao processar o registro de movimento do histórico do cliente"));
        assertTrue(exception.getMessage().contains("dynamo unavailable"));
    }
}
