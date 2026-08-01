package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.in.mappers.LoanMapperImpl;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.Loan;
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

    @Override
    public CustomerLoanOutput checkLoanEligibility(CustomerLoanInput input) {
        log.info("Checking loan eligibility for customer: {}", input.toCustomer().getName());

        final var loan = loanMapper.toLoan(input.toCustomer());
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
