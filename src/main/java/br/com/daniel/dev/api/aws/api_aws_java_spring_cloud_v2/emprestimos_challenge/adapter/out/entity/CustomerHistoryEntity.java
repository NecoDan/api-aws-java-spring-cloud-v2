package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.entity;

import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.io.Serializable;

@DynamoDbBean
@Setter
public class CustomerHistoryEntity implements Serializable {

    private String id;
    private String identificadorCliente;
    private String dtUltimaAtualizacao;
    private String conteudoEmprestimos;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("cod_idef")
    public String getId() {
        return id;
    }

    @DynamoDbAttribute("cod_idef_cli")
    public String getIdentificadorCliente() {
        return identificadorCliente;
    }

    @DynamoDbAttribute("data_ultima_atualizacao")
    public String getDtUltimaAtualizacao() {
        return dtUltimaAtualizacao;
    }

    @DynamoDbAttribute("payload_emprestimos")
    public String getConteudoEmprestimos() {
        return conteudoEmprestimos;
    }

}
