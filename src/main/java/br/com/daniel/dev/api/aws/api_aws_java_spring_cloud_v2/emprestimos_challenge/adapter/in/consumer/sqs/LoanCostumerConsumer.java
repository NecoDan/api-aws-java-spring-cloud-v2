package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.in.consumer.sqs;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.exceptions.EmprestimoCustomerException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.CustomerMovementRecordUseCase;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.input.CustomerHistoryMovementInput;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.logs.MdcUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "sqs.consumer.loan-costumer.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class LoanCostumerConsumer {

    private final CustomerMovementRecordUseCase customerMovementRecordUseCase;
    private final ObjectMapper objectMapper;

    /**
     * Consome mensagens de uma fila SQS e processa os dados recebidos.
     *
     * @param acknowledgement Objeto para confirmar manualmente o processamento da mensagem.
     * @param sqsMessage      A mensagem recebida da fila SQS em formato de string.
     */
    @SqsListener(
            value = "${fila.receiver.name.sqs-solicitar-regitro-movimento-cliente}",
            acknowledgementMode = "MANUAL",
            maxConcurrentMessages = "5",
            pollTimeoutSeconds = "5",
            maxMessagesPerPoll = "5"
    )
    public void consumer(Acknowledgement acknowledgement, String sqsMessage) {
        MdcUtils.putTransactionIdRandom();
        log.info("Loan & costumer - Received message from messsage: {}", sqsMessage);

        try {
            final var input = objectMapper.readValue(sqsMessage, CustomerHistoryMovementInput.class);
            customerMovementRecordUseCase.execute(input);

            acknowledgement.acknowledge();
        } catch (EmprestimoCustomerException e) {
            final var errorMessage = e.getCause() != null ? e.getCause().toString() : "Cause not available";
            log.error("Error processing message from SQS: {}. Cause: {}. Error: {}", sqsMessage, e, errorMessage, e);

            acknowledgement.acknowledge();
        } catch (Exception e) {
            log.error("Unexpected error while processing message from SQS: {}. Error: {}", sqsMessage, e.getCause(), e);
        } finally {
            MdcUtils.clear();
        }
    }
}
