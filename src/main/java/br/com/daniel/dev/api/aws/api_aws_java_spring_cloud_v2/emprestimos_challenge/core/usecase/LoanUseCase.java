package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.Loan;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.input.CustomerLoanInput;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.output.CustomerLoanOutput;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.output.LoanOutput;

import java.util.List;

/**
 * Interface that defines the use case for loan-related operations.
 */
public interface LoanUseCase {

    /**
     * Checks the loan eligibility for a customer based on the provided input.
     *
     * @param request The input containing customer details for loan eligibility check.
     * @return A {@link CustomerLoanOutput} containing the result of the eligibility check.
     */
    CustomerLoanOutput checkLoanEligibility(CustomerLoanInput request);


    /**
     * Retrieves a list of available loans for the given loan details.
     *
     * @param loan The loan object containing details to determine available loans.
     * @return A list of {@link LoanOutput} representing the available loans.
     */
    List<LoanOutput> getListOfAvailableLoans(Loan loan);
}
