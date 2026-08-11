package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerHistory implements Serializable {

    private String id;
    private String dtUltimaAtualizacao;
    private String conteudoEmprestimos;

    public void gerarDataAtualizacao() {
        this.dtUltimaAtualizacao = LocalDateTime.now().toString();
    }

    public CustomerHistory gerarDtAtualizacao() {
        gerarDataAtualizacao();
        return this;
    }
}
