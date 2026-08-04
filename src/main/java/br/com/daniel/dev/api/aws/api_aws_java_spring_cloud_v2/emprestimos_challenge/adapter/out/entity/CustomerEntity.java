package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.entity;

import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@DynamoDbBean
@Setter
public class CustomerEntity implements Serializable {

    @Serial private static final long serialVersionUID = -1607433520779306156L;

    private String id;
    private String dtCriacao;
    private Integer age;
    private String cpf;
    private String name;
    private BigDecimal income;
    private String location;
    private String dtAtualizacao;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("cod_idef_cliente")
    public String getId() {
        return id;
    }

    @DynamoDbAttribute("data_criacao")
    public String getDtCriacao() {
        return dtCriacao;
    }

    @DynamoDbAttribute("idade")
    public Integer getAge() {
        return age;
    }

    @DynamoDbAttribute("num_documento_cpf")
    public String getCpf() {
        return cpf;
    }

    @DynamoDbAttribute("nome")
    public String getName() {
        return name;
    }

    @DynamoDbAttribute("valor_renda")
    public BigDecimal getIncome() {
        return income;
    }

    @DynamoDbAttribute("sigla_estado")
    public String getLocation() {
        return location;
    }

    @DynamoDbAttribute("data_atualizacao")
    public String getDtAtualizacao() {
        return dtAtualizacao;
    }
}
