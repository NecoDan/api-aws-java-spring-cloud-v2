package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.exceptions.LoanNotAvailableException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.enums.LoanType;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.enums.LoanTypePercent;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.enums.TipoEstado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Loan implements Serializable {

    @Serial private static final long serialVersionUID = 1666768725052453109L;

    private static final double VALOR_TRES_MIL = 3000.00D;
    private static final double VALOR_CINCO_MIL = 5000.00D;

    private Customer customer;
    private LoanTypePercent loanTypePercent;
    private LoanType loanType;
    private Double interestRate;

    private boolean isCustomerValid() {
        return Objects.nonNull(this.customer);
    }

    public Loan(Loan loan) {
        this.customer = loan.getCustomer();
        this.loanTypePercent = loan.getLoanTypePercent();
        this.loanType = loan.getLoanType();
        this.interestRate = loan.getInterestRate();
    }

    public Loan toAvailableLoansPersonal() {
        if (isPersonalLoanAvailable()) {
            this.loanType = LoanType.PERSONAL;
            this.interestRate = getPersonalLoanInterestRate();
        }
        return this;
    }

    public Loan toAvailableLoansConsignment() {
        if (isConsignmentLoanAvailable()) {
            this.loanType = LoanType.CONSIGNMENT;
            this.interestRate = LoanTypePercent.CONSIGNMENT_PERCENT.getPercent();
        }
        return this;
    }

    public Loan toAvaliableLoansGuaranteed() {
        if (isGuaranteedLoanAvailable()) {
            this.loanType = LoanType.GUARANTEED;
            this.interestRate = LoanTypePercent.GUARANTEED_PERCENT.getPercent();
        }
        return this;
    }

    public boolean isPersonalLoanAvailable() {
        return isCustomerValid()
                && this.customer.isIncomeEqualOrLowerThan(BigDecimal.valueOf(VALOR_TRES_MIL))
                || isPersonalOrGuaranteedLoanAvailableSecondTerms();
    }

    public boolean isConsignmentLoanAvailable() {
        return isCustomerValid()
                && this.customer.isIncomeEqualOrGreaterThan(BigDecimal.valueOf(VALOR_CINCO_MIL));
    }

    public boolean isGuaranteedLoanAvailable() {
        return isCustomerValid()
                && this.customer.isIncomeEqualOrLowerThan(BigDecimal.valueOf(VALOR_TRES_MIL))
                || isPersonalOrGuaranteedLoanAvailableSecondTerms();
    }

    public boolean isPersonalOrGuaranteedLoanAvailableSecondTerms() {
        return isCustomerValid()
                && this.customer.isIncomeBetween(BigDecimal.valueOf(VALOR_TRES_MIL), BigDecimal.valueOf(VALOR_CINCO_MIL))
                && this.customer.isAgeLowerThan(30)
                && this.customer.isFromLocation(TipoEstado.SP.getCodigo());
    }

    public double getPersonalLoanInterestRate() {
        if (isPersonalLoanAvailable()) {
            return LoanTypePercent.PERSONAL_PERCENT.getPercent();
        }

        throw new LoanNotAvailableException("Personal loan is not available for this customer.");
    }
}
