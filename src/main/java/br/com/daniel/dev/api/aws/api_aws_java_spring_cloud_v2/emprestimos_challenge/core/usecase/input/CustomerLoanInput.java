package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.input;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.Customer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

public record CustomerLoanInput(@NotNull @Min(value = 18) Integer age,
                                @CPF String cpf,
                                @NotBlank String name,
                                @NotNull @Min(value = 1000) BigDecimal income,
                                @NotBlank String location) {

    public Customer toCustomer() {
        return new Customer(age, cpf, name, income, location);
    }
}
