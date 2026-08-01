package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.output;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.Loan;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.enums.LoanType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public record LoanOutput(LoanType type,
                         @JsonProperty("interest_rate") Double interestRate) {

    @JsonIgnore
    public static LoanOutput toLoanResponseFrom(Loan loan) {
        if (Objects.nonNull(loan)) {
            return new LoanOutput(loan.getLoanType(), loan.getInterestRate());
        }
        return null;
    }

    @JsonIgnore
    public boolean isValidParams() {
        return Objects.nonNull(this.type) && Objects.nonNull(this.interestRate) && this.interestRate > 0;
    }
}
