package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.port;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model.Customer;

import java.util.Optional;

public interface CustomerPort {
    void salvar(Customer customer);

    void atualizar(Customer customer);

    Optional<Customer> buscarPorId(String id);

    Optional<Customer> buscarPorNumeroDocumentoCpf(String cpf);
}
