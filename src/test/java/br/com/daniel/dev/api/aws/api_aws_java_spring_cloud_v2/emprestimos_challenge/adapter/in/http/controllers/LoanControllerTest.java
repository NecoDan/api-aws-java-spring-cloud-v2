package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.in.http.controllers;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.enums.LoanType;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.LoanUseCase;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.input.CustomerLoanInput;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.output.CustomerLoanOutput;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.output.LoanOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

    @Mock
    private LoanUseCase loanUseCase;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new LoanController(loanUseCase)).build();
    }

    @Test
    void shouldCheckLoanEligibilityForCustomer() throws Exception {
        CustomerLoanInput request = new CustomerLoanInput(
                28,
                "12345678909",
                "Ana Silva",
                new BigDecimal("5000.00"),
                "SP"
        );

        CustomerLoanOutput response = new CustomerLoanOutput(
                "customer-123",
                "Ana Silva",
                List.of(new LoanOutput(LoanType.PERSONAL, 2.9D))
        );

        when(loanUseCase.checkLoanEligibility(any(CustomerLoanInput.class))).thenReturn(response);

        mockMvc.perform(post("/loans/customer-loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerIdentifier").value("customer-123"))
                .andExpect(jsonPath("$.customer").value("Ana Silva"))
                .andExpect(jsonPath("$.loans[0].type").value("PERSONAL"))
                .andExpect(jsonPath("$.loans[0].interest_rate").value(2.9));

        verify(loanUseCase).checkLoanEligibility(any(CustomerLoanInput.class));
    }
}
