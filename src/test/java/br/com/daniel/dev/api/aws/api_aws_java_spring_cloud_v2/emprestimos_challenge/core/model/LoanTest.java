package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.util.LoanChallengeFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    @Test
    void isPersonalLoanAvailable_IsTrue() {
        final var locationEstadoSaoPaulo = "SP";
        final var loanPositiveWithSP = LoanChallengeFactory.createLoanDomainIncomeIdadePositivo(locationEstadoSaoPaulo);

        assertNotNull(loanPositiveWithSP);
        assertTrue(loanPositiveWithSP.isPersonalLoanAvailable());
    }

    @Test
    void isPersonalLoanAvailable_IsFalseV1() {
        final var locationEstadoMaranhao = "MA";
        final var loanNegativeWithMA = LoanChallengeFactory.createLoanDomainIncomeMaiorQue3000(locationEstadoMaranhao);

        assertNotNull(loanNegativeWithMA);
        assertFalse(loanNegativeWithMA.isPersonalLoanAvailable());
    }

    @Test
    void isPersonalLoanAvailable_IsFalseV2() {
        final var loanVar145 = LoanChallengeFactory.createLoanDomainIncomeIdadeNegativo("");
        loanVar145.setCustomer(null);

        assertNotNull(loanVar145);
        assertFalse(loanVar145.isPersonalLoanAvailable());
    }
}