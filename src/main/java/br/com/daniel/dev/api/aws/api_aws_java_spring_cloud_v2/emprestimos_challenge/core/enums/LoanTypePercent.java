package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.enums;


import lombok.Getter;

@Getter
public enum LoanTypePercent {

    PERSONAL_PERCENT(LoanType.PERSONAL, 4.0),

    GUARANTEED_PERCENT(LoanType.GUARANTEED, 3.0),

    CONSIGNMENT_PERCENT(LoanType.CONSIGNMENT, 2.0);

    private final Double percent;
    private final LoanType loanType;

    LoanTypePercent(LoanType loanType, Double percent) {
        this.percent = percent;
        this.loanType = loanType;
    }
}
