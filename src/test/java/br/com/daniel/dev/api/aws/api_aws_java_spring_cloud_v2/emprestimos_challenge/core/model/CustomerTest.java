package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.util.LoanChallengeFactory;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.enums.TipoEstado;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void shoulCreateInstanceCustomerValidByBuild(){

        var customer = Customer.builder()
                .cpf("85788718040")
                .name("Marcos Aurelio Costa")
                .income(BigDecimal.valueOf(RandomUtils.secure().randomDouble(100.0, 1000.0)))
                .age(RandomUtils.secureStrong().randomInt(10, 100))
                .location(TipoEstado.AM.getCodigo())
                .build();

        assertNotNull(customer);
        assertNotNull(customer.getAge());
        assertNotNull(customer.getCpf());
        assertNotNull(customer.getName());
        assertNotNull(customer.getIncome());
        assertNotNull(customer.getLocation());
    }

    @Test
    void shouldReturnTrueWhenIncomeIsGreaterThanValue() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(BigDecimal.valueOf(8000.00));
        final var valueToCompare = new BigDecimal("5000.00");

        assertThat(customer.isIncomeEqualOrGreaterThan(valueToCompare))
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenIncomeIsEqualGreaterToValue() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(BigDecimal.valueOf(3000.00));
        BigDecimal valueToCompare = new BigDecimal("3000.00");

        assertThat(customer.isIncomeEqualOrGreaterThan(valueToCompare))
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenIncomeIsGreaterThanValue() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(BigDecimal.valueOf(1000.00));
        BigDecimal valueToCompare = new BigDecimal("20000.00");

        assertThat(customer.isIncomeEqualOrGreaterThan(valueToCompare))
                .isFalse();
    }

    @Test
    void shouldReturnFalseWhenParamValueIsNullV1() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(BigDecimal.valueOf(2000.00));
        assertThat(customer.isIncomeEqualOrGreaterThan(null))
                .isFalse();
    }

    @Test
    void shouldReturnFalseWhenCustomerIncomeIsNullV1() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(null);
        BigDecimal valueToCompare = new BigDecimal("8000.00");

        assertThat(customer.isIncomeEqualOrGreaterThan(valueToCompare)).isFalse();
    }

    @Test
    void shouldReturnTrueWhenIncomeIsLowerThanValue() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(BigDecimal.valueOf(1500.00));
        BigDecimal valueToCompare = new BigDecimal("2000.00");

        assertThat(customer.isIncomeEqualOrLowerThan(valueToCompare))
                .isTrue();
    }

    @Test
    void shouldReturnTrueWhenIncomeIsEqualToValue() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(BigDecimal.valueOf(2000.00));
        BigDecimal valueToCompare = new BigDecimal("2000.00");

        assertThat(customer.isIncomeEqualOrLowerThan(valueToCompare))
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenIncomeIsThanValue() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(BigDecimal.valueOf(2500.00));
        BigDecimal valueToCompare = new BigDecimal("2000.00");

        assertThat(customer.isIncomeEqualOrLowerThan(valueToCompare))
                .isFalse();
    }

    @Test
    void shouldReturnFalseWhenParamValueIsNull() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(BigDecimal.valueOf(2000.00));
        assertThat(customer.isIncomeEqualOrLowerThan(null))
                .isFalse();
    }

    @Test
    void shouldReturnFalseWhenCustomerIncomeIsNull() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(null);
        BigDecimal valueToCompare = new BigDecimal("2000.00");

        assertThat(customer.isIncomeEqualOrLowerThan(valueToCompare)).isFalse();
    }

    @Test
    void shouldReturnTrueWhenIncomeIsExactlyMinValue() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(BigDecimal.valueOf(1000.00));
        BigDecimal min = new BigDecimal("1000.00");
        BigDecimal max = new BigDecimal("2000.00");

        assertThat(customer.isIncomeBetween(min, max)).isTrue();
    }

    @Test
    void shouldReturnTrueWhenIncomeIsBetweenMinAndMax() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(BigDecimal.valueOf(1500.00));
        BigDecimal min = new BigDecimal("1000.00");
        BigDecimal max = new BigDecimal("2000.00");

        assertThat(customer.isIncomeBetween(min, max)).isTrue();
    }

    @Test
    void shouldReturnFalseWhenIncomeIsExactlyMaxValue() {
        // O método usa '< max', então o limite superior é exclusivo
        final var customer = LoanChallengeFactory.createCustomerWithIncome(BigDecimal.valueOf(2000.00));
        BigDecimal min = new BigDecimal("1000.00");
        BigDecimal max = new BigDecimal("2000.00");

        assertThat(customer.isIncomeBetween(min, max)).isFalse();
    }

    @Test
    void shouldReturnFalseWhenIncomeIsBelowMin() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(BigDecimal.valueOf(500.00));
        BigDecimal min = new BigDecimal("1000.00");
        BigDecimal max = new BigDecimal("2000.00");

        assertThat(customer.isIncomeBetween(min, max)).isFalse();
    }

    @Test
    void shouldReturnFalseWhenMinOrMaxValuesAreNull() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(BigDecimal.valueOf(1500.00));
        BigDecimal validValue = new BigDecimal("1000.00");

        assertThat(customer.isIncomeBetween(null, validValue))
                .isFalse();

        assertThat(customer.isIncomeBetween(validValue, null))
                .isFalse();
    }

    @Test
    void shouldReturnFalseWhenCustomerIncomeIsNullV2() {
        final var customer = LoanChallengeFactory.createCustomerWithIncome(null);
        BigDecimal min = new BigDecimal("1000.00");
        BigDecimal max = new BigDecimal("2000.00");

        assertThat(customer.isIncomeBetween(min, max)).isFalse();
    }


    @Test
    void shouldReturnTrueWhenCustomerAgeIsLower() {
        final var customer = LoanChallengeFactory.createCustomerWithAge(25);
        assertThat(customer.isAgeLowerThan(30)).isTrue();
    }

    @Test
    void shouldReturnFalseWhenCustomerAgeIsEqual() {
        final var customer = LoanChallengeFactory.createCustomerWithAge(30);
        assertThat(customer.isAgeLowerThan(30)).isFalse();
    }

    @Test
    void shouldReturnFalseWhenCustomerAgeIsGreater() {
        final var customer = LoanChallengeFactory.createCustomerWithAge(35);
        assertThat(customer.isAgeLowerThan(30)).isFalse();
    }

    @Test
    void shouldReturnFalseWhenParamAgeIsNull() {
        final var customer = LoanChallengeFactory.createCustomerWithAge(25);
        assertThat(customer.isAgeLowerThan(null)).isFalse();
    }

    @Test
    void shouldReturnFalseWhenCustomerAgeIsNull() {
        final var customer = LoanChallengeFactory.createCustomerWithAge(null);
        assertThat(customer.isAgeLowerThan(30)).isFalse();
    }

    @Test
    void shouldReturnTrueWhenLocationMatchesExactly() {
        final var customer = LoanChallengeFactory.createCustomerWithLocation("SP");
        assertThat(customer.isFromLocation("SP")).isTrue();
    }

    @Test
    void shouldReturnTrueWhenLocationMatchesIgnoringCase() {
        final var customer = LoanChallengeFactory.createCustomerWithLocation("rj");
        assertThat(customer.isFromLocation("RJ")).isTrue();
    }

    @Test
    void shouldReturnFalseWhenLocationDoesNotMatch() {
        final var customer = LoanChallengeFactory.createCustomerWithLocation("MG");
        assertThat(customer.isFromLocation("BA")).isFalse();
    }

    @Test
    void shouldReturnFalseWhenCustomerLocationIsNull() {
        final var customer = LoanChallengeFactory.createCustomerWithLocation(null);
        assertThat(customer.isFromLocation("SP")).isFalse();
    }

    @Test
    void shouldReturnFalseWhenParamLocationIsNull() {
        final var customer = LoanChallengeFactory.createCustomerWithLocation("SP");
        assertThat(customer.isFromLocation(null)).isFalse();
    }

}