package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.input;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.output.LoanOutput;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.FunctionalUtils;

import java.util.List;

public record CustomerHistoryMovementInput(
        String customerIdentifier,
        List<LoanOutput> loans
) {

    public String toJsonString() {
        return FunctionalUtils.toStringJson(this.getClass());
    }
}
