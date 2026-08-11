package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.usecase.input.CustomerHistoryMovementInput;

public interface CustomerMovementRecordUseCase {

    void execute(CustomerHistoryMovementInput input);
}
