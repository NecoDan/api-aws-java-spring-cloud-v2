package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.in.http.controllers;


import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.LoanUseCase;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.input.CustomerLoanInput;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.output.CustomerLoanOutput;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.logs.MdcUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/loans")
public class LoanController {

    private final LoanUseCase loanUseCase;

    @PostMapping(value = "/customer-loans")
    @Operation(summary = "Cria uma nova solicitacao emprestimos", description = "Cria uma nova solicitacao emprestimos disponiveis ao cliente com os dados fornecidos no corpo da requisição.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitacao criada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Campos não atendem os requisitos da transação"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<CustomerLoanOutput> customersLoans(@Valid @RequestBody CustomerLoanInput request) {
        try {
            MdcUtils.putTransactionIdRandom();
            return ResponseEntity.ok().body(loanUseCase.checkLoanEligibility(request));
        } finally {
            MdcUtils.clear();
        }
    }
}
