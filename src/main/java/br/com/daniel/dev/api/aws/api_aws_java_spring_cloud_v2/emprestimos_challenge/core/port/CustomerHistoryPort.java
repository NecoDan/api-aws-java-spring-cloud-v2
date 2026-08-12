package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.port;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.CustomerHistory;

import java.util.List;

public interface CustomerHistoryPort {

    /**
     * Salva o histórico de movimentação de um cliente.
     *
     * @param customerHistory Objeto que contém as informações do histórico de movimentação do cliente.
     */
    void salvar(CustomerHistory customerHistory);

    /**
     * Busca os movimentos históricos de um cliente com base no identificador fornecido.
     *
     * @param identificadorCliente Identificador único do cliente.
     * @return Lista de objetos `CustomerHistory` representando os movimentos históricos do cliente.
     */
    List<CustomerHistory> buscarMovimentosPor(String identificadorCliente);
}
