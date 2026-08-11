package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.port;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.CustomerHistory;

public interface CustomerHistoryPort {
    void salvar(CustomerHistory customerHistory);
}
