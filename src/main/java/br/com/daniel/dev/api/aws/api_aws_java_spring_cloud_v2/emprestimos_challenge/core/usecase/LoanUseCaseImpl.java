package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.mappers.LoanMapperImpl;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.Customer;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.Loan;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.port.CustomerPort;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.input.CustomerLoanInput;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.output.CustomerLoanOutput;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.output.LoanOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanUseCaseImpl implements LoanUseCase {

    private final LoanMapperImpl loanMapper;
    private final CustomerPort customerPort;

    @Override
    public CustomerLoanOutput checkLoanEligibility(CustomerLoanInput input) {
        log.info("Checking loan eligibility for customer: {}", input.name());
        final var optionalCustomer = customerPort.buscarPorNumeroDocumentoCpf(input.cpf());

        if (optionalCustomer.isEmpty()) {
            final var customerSaved = input.toCustomer();
            customerPort.salvar(customerSaved);
            return finalizeCheckLoanEligibility(customerSaved);
        }

        var customerUpdated = optionalCustomer.get();
        updateCustomerData(customerUpdated, input);

        return finalizeCheckLoanEligibility(customerUpdated);
    }

    private void updateCustomerData(Customer customerUpdate, CustomerLoanInput input) {
        log.info("Atualizar dados cliente para o ID: {}", customerUpdate.getId());

        if (input.age() > customerUpdate.getAge()) {
            customerUpdate.setAge(input.age());
        }

        customerUpdate.setName(input.name());
        customerUpdate.setIncome(input.income());
        customerUpdate.setLocation(input.location());
        customerUpdate.generateDtAtualizacao();

        customerPort.atualizar(customerUpdate);
    }

    private CustomerLoanOutput finalizeCheckLoanEligibility(Customer customer) {
        final var loan = loanMapper.toLoan(customer);
        log.info("Loan eligibility check completed for customer: {}. Available loans: {}", loan.getCustomer().getName(), getListOfAvailableLoans(loan));
        return new CustomerLoanOutput(loan.getCustomer().getName(), getListOfAvailableLoans(loan));
    }

    @Override
    public List<LoanOutput> getListOfAvailableLoans(Loan loan) {
        log.info("Retrieving list of available loans for customer: {}", loan.getCustomer().getName());
        List<LoanOutput> list = new ArrayList<>();

        list.add(LoanOutput.toLoanResponseFrom(new Loan(loan.toAvailableLoansPersonal())));
        list.add(LoanOutput.toLoanResponseFrom(new Loan(loan.toAvaliableLoansGuaranteed())));
        list.add(LoanOutput.toLoanResponseFrom(new Loan(loan.toAvailableLoansConsignment())));

        return list.stream()
                .filter(Objects::nonNull)
                .filter(LoanOutput::isValidParams)
                .distinct()
                .toList();
    }
}
