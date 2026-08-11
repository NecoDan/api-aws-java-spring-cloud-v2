package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.output;

import java.util.List;

public record CustomerLoanOutput(
        String customerIdentifier,
        String customer,
        List<LoanOutput> loans) {
}
