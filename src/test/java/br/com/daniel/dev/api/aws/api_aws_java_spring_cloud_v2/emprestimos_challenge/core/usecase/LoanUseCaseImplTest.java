package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.mappers.LoanMapperImpl;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.enums.LoanType;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.Customer;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.port.CustomerPort;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.input.CustomerLoanInput;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.enums.TipoEstado;
import com.github.javafaker.Faker;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanUseCaseImplTest {

    @Mock
    private LoanMapperImpl loanMapperMock;

    @Mock
    private CustomerPort customerPort;

    @InjectMocks
    private LoanUseCaseImpl loanUseCase;

    private LoanMapperImpl loanMapper;

    private static final Faker FAKER = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.loanMapper = new LoanMapperImpl();
    }

    @Test
    void checkLoanEligibilityContemAvailableLoansPersonal() {
        // -- 01_Cenário
        var customerLoanRequest = new CustomerLoanInput(
                RandomUtils.secure().randomInt(16, 30),
                getNumeroCpfMock(),
                FAKER.name().fullName(),
                getValueRoundingMode(3000.0D),
                TipoEstado.SP.getCodigo()
        );

        final var loan = this.loanMapper.toLoan(customerLoanRequest.toCustomer());

        when(loanMapperMock.toLoan(any(Customer.class)))
                .thenReturn(loan);

        // -- 02_Ação
        final var customerLoanResponse = loanUseCase.checkLoanEligibility(customerLoanRequest);

        // -- 03_Verificação_Validação
        assertNotNull(customerLoanResponse.customer());
        assertTrue(StringUtils.isNotEmpty(customerLoanResponse.customer()));
        assertTrue(CollectionUtils.isNotEmpty(customerLoanResponse.loans()));
        assertEquals(2, customerLoanResponse.loans().size());

        customerLoanResponse.loans()
                .forEach(loanResponse -> {
                    assertNotNull(loanResponse.type());
                    assertTrue(LoanType.PERSONAL.equals(loanResponse.type()) || LoanType.GUARANTEED.equals(loanResponse.type()));
                    assertNotNull(loanResponse.interestRate());
                });

        System.out.println(customerLoanResponse);
    }

    @Test
    void checkLoanEligibilityContemAvailableLoansGuaranteed() {
        // -- 01_Cenário
        var customerLoanRequest = new CustomerLoanInput(
                RandomUtils.secure().randomInt(16, 30),
                getNumeroCpfMock(),
                FAKER.name().fullName(),
                getValueRoundingMode(4500.0D),
                TipoEstado.SP.getCodigo()
        );

        final var loan = this.loanMapper.toLoan(customerLoanRequest.toCustomer());

        when(loanMapperMock.toLoan(any(Customer.class)))
                .thenReturn(loan);

        // -- 02_Ação
        final var customerLoanResponse = loanUseCase.checkLoanEligibility(customerLoanRequest);

        // -- 03_Verificação_Validação
        assertNotNull(customerLoanResponse.customer());
        assertTrue(StringUtils.isNotEmpty(customerLoanResponse.customer()));
        assertTrue(CollectionUtils.isNotEmpty(customerLoanResponse.loans()));
        assertEquals(2, customerLoanResponse.loans().size());

        customerLoanResponse.loans()
                .forEach(loanResponse -> {
                    assertNotNull(loanResponse.type());
                    assertTrue(LoanType.PERSONAL.equals(loanResponse.type()) || LoanType.GUARANTEED.equals(loanResponse.type()));
                    assertNotNull(loanResponse.interestRate());
                });

        System.out.println(customerLoanResponse);
    }

    @Test
    void checkLoanEligibilityContemAvailableLoansConsignment() {
        // -- 01_Cenário
        var customerLoanRequest = new CustomerLoanInput(
                RandomUtils.secure().randomInt(31, 100),
                getNumeroCpfMock(),
                FAKER.name().fullName(),
                getValueRoundingMode(8400.0D),
                TipoEstado.randomTipoEstado().getCodigo()
        );

        final var loan = this.loanMapper.toLoan(customerLoanRequest.toCustomer());

        when(loanMapperMock.toLoan(any(Customer.class)))
                .thenReturn(loan);

        // -- 02_Ação
        final var customerLoanResponse = loanUseCase.checkLoanEligibility(customerLoanRequest);

        // -- 03_Verificação_Validação
        assertNotNull(customerLoanResponse.customer());
        assertTrue(StringUtils.isNotEmpty(customerLoanResponse.customer()));
        assertTrue(CollectionUtils.isNotEmpty(customerLoanResponse.loans()));
        assertEquals(1, customerLoanResponse.loans().size());

        customerLoanResponse.loans()
                .forEach(loanResponse -> {
                    assertNotNull(loanResponse.type());
                    assertEquals(LoanType.CONSIGNMENT, loanResponse.type());
                    assertNotNull(loanResponse.interestRate());
                });

        System.out.println(customerLoanResponse);
    }

    @Test
    void getListOfAvailableLoans() {
        // -- 01_Cenário
        var customerLoanRequest = new CustomerLoanInput(
                RandomUtils.secure().randomInt(12, 30),
                getNumeroCpfMock(),
                FAKER.name().fullName(),
                getValueRoundingMode(3800.0D),
                TipoEstado.SP.getCodigo()
        );

        final var loan = this.loanMapper.toLoan(customerLoanRequest.toCustomer());

        // -- 02_Ação
        final var listOfAvailableLoans = loanUseCase.getListOfAvailableLoans(loan);

        // -- 03_Verificação_Validação
        assertNotNull(listOfAvailableLoans);
        assertTrue(CollectionUtils.isNotEmpty(listOfAvailableLoans));
        assertEquals(2, listOfAvailableLoans.size());

        listOfAvailableLoans
                .forEach(loanResponse -> {
                    assertNotNull(loanResponse.type());
                    assertTrue(LoanType.PERSONAL.equals(loanResponse.type()) || LoanType.GUARANTEED.equals(loanResponse.type()));
                    assertNotNull(loanResponse.interestRate());
                });

        System.out.println(listOfAvailableLoans);
    }

    @Test
    void shouldSaveNewCustomerWhenCpfIsNotRegistered() {
        var customerLoanRequest = new CustomerLoanInput(
                28,
                "64810674061",
                "Ana Silva",
                getValueRoundingMode(5000.0D),
                TipoEstado.SP.getCodigo()
        );
        var loan = loanMapper.toLoan(customerLoanRequest.toCustomer());
        when(customerPort.buscarPorNumeroDocumentoCpf(customerLoanRequest.cpf())).thenReturn(java.util.Optional.empty());
        when(loanMapperMock.toLoan(any(Customer.class))).thenReturn(loan);

        var response = loanUseCase.checkLoanEligibility(customerLoanRequest);

        assertNotNull(response);
        assertEquals("Ana Silva", response.customer());
        assertEquals(1, response.loans().size());
        assertEquals(LoanType.CONSIGNMENT, response.loans().getFirst().type());
        verify(customerPort).salvar(any(Customer.class));
        verify(customerPort, never()).atualizar(any(Customer.class));
    }

    @Test
    void shouldUpdateExistingCustomerWhenCpfAlreadyExists() {
        var customerLoanRequest = new CustomerLoanInput(
                32,
                "64810674061",
                "Maria Souza",
                getValueRoundingMode(4200.0D),
                TipoEstado.SP.getCodigo()
        );
        var existingCustomer = Customer.builder()
                .id("customer-123")
                .age(28)
                .cpf(customerLoanRequest.cpf())
                .name("Maria Antonia")
                .income(getValueRoundingMode(2500.0D))
                .location(TipoEstado.RJ.getCodigo())
                .dtCriacao("2024-01-01T00:00:00")
                .build();

        when(customerPort.buscarPorNumeroDocumentoCpf(customerLoanRequest.cpf())).thenReturn(java.util.Optional.of(existingCustomer));
        when(loanMapperMock.toLoan(any(Customer.class))).thenAnswer(invocation -> loanMapper.toLoan(invocation.getArgument(0)));

        var response = loanUseCase.checkLoanEligibility(customerLoanRequest);

        assertNotNull(response);
        assertEquals("Maria Souza", existingCustomer.getName());
        assertEquals(32, existingCustomer.getAge());
        assertEquals(customerLoanRequest.income(), existingCustomer.getIncome());
        assertEquals(TipoEstado.SP.getCodigo(), existingCustomer.getLocation());
        assertNotNull(existingCustomer.getDtAtualizacao());
        assertNotNull(response.customer());
        verify(customerPort).atualizar(existingCustomer);
    }

    private BigDecimal getValueRoundingMode(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    private String getNumeroCpfMock() {
        return "0".concat(FAKER.idNumber().valid().replace("-", ""));
    }

    private CustomerLoanInput mockCustomerLoanRequest() {
        return new CustomerLoanInput(RandomUtils.secure().randomInt(16, 100),
                "0".concat(FAKER.idNumber().valid().replace("-", "")),
                FAKER.name().fullName(),
                BigDecimal.valueOf(RandomUtils.secure().randomDouble(100.0D, 1000.0D)).setScale(2, RoundingMode.HALF_UP),
                TipoEstado.randomTipoEstado().getCodigo()
        );
    }

}