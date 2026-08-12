package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.exceptions.EmprestimoCustomerException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.CustomerHistory;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.port.CustomerS3DataPort;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.port.CustomerHistoryPort;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.port.CustomerPort;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.input.CustomerHistoryMovementInput;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerMovementRecordUseCaseImpl implements CustomerMovementRecordUseCase {

    private final CustomerHistoryPort customerHistoryPort;
    private final CustomerS3DataPort customerS3DataPort;
    private final CustomerPort customerPort;

    @Value("${arquivo.s3.bucket.name.bucket-reports-mov-cli}")
    private String bucketName;

    @Override
    public void execute(CustomerHistoryMovementInput input) {
        try {
            log.info("Inicializando o fluxo de registro de movimento do cliente com a entrada: {}", input);

            final var customer = customerPort.buscarPorId(input.customerIdentifier())
                    .orElseThrow(() ->
                            new EmprestimoCustomerException("Cliente não encontrado para o identificador: %s"
                                    .formatted(input.customerIdentifier())
                            )
                    );

            log.info("Cliente encontrado com o ID: {}, para seguir com o fluxo.", customer.getId());
            var customerHistory = buildCustomerHistory(input);

            customerHistoryPort.salvar(customerHistory);
            processarDadosEnvioBucket(customerHistory);

            log.info("Fluxo de registro de movimento do cliente concluído com sucesso para o cliente.");
        } catch (Exception e) {
            log.error("Falhar ao processar o registro de movimento do histórico do cliente: {}. Detalhes do erro: {}",
                    input.customerIdentifier(),
                    e.getMessage(), e
            );

            throw new EmprestimoCustomerException("Falhar ao processar o registro de movimento do histórico do cliente: "
                    + input.customerIdentifier()
                    + ". Detalhes do erro: "
                    + e.getMessage(), e
            );
        }
    }

    private void processarDadosEnvioBucket(CustomerHistory customerHistory) {
        try {
            log.info("Inicializando processamento relatorio para envio dados bucket AWS S3 por cliente ID: {}",
                    customerHistory.getIdentificadorCliente());

            final var historyList = customerHistoryPort.buscarMovimentosPor(customerHistory.getIdentificadorCliente());

            if (historyList.isEmpty()) {
                log.warn("Histórico movimentos nao encontrado(s) ao cliente ID: {}. Nenhum arquivo será processadoe/gerado.", customerHistory.getIdentificadorCliente());
                return;
            }

            byte[] conteudo = FileUtils.generateCsvFile(buildDataForCsv(historyList));
            var nomeArquivo = "historico_movimentos_cliente_" + customerHistory.getIdentificadorCliente() + ".csv";
            var multipartFileBytes = FileUtils.createFromBytes(conteudo, nomeArquivo, "text/csv");

            customerS3DataPort.enviarDadosBucket(bucketName, nomeArquivo, multipartFileBytes);
        } catch (Exception e) {
            throw new EmprestimoCustomerException("Falha ao processar o envio de dados para bucket AWS S3 para o cliente ID: "
                    + customerHistory.getIdentificadorCliente()
                    + ". Detalhes do erro: "
                    + e.getMessage(), e
            );
        }
    }

    private CustomerHistory buildCustomerHistory(CustomerHistoryMovementInput input) {
        return CustomerHistory.builder()
                .identificadorCliente(input.customerIdentifier())
                .conteudoEmprestimos(input.toJsonString())
                .build()
                .gerarId()
                .gerarDtAtualizacao();
    }

    private List<String[]> buildDataForCsv(List<CustomerHistory> historyList) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "Cliente", "Data_Atualizaao", "Emprestimos"});

        historyList.forEach(
                customerHistoryLine -> data.add(new String[]{
                                customerHistoryLine.getId(),
                                customerHistoryLine.getIdentificadorCliente(),
                                customerHistoryLine.getConteudoEmprestimos(),
                                customerHistoryLine.getDtUltimaAtualizacao()
                        }
                )
        );

        return data;
    }
}
