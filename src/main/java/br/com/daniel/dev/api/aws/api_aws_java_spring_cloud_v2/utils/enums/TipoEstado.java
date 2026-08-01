package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.enums;

import lombok.Getter;

import java.util.Random;
import java.util.stream.Stream;

@Getter
public enum TipoEstado {

    AC("AC", "Acre"),
    AL("AL", "Alagoas"),
    AP("AP", "Amapá"),
    AM("AM", "Amazonas"),
    BA("BA", "Bahia"),
    CE("CE", "Ceará"),
    DF("DF", "Distrito Federal"),
    ES("ES", "Espírito Santo"),
    GO("GO", "Goiás"),
    MA("MA", "Maranhão"),
    MT("MT", "Mato Grosso"),
    MS("MS", "Mato Grosso do Sul"),
    MG("MG", "Minas Gerais"),
    PA("PA", "Pará"),
    PB("PB", "Paraíba"),
    PR("PR", "Paraná"),
    PE("PE", "Pernambuco"),
    PI("PI", "Piauí"),
    RJ("RJ", "Rio de Janeiro"),
    RN("RN", "Rio Grande do Norte"),
    RS("RS", "Rio Grande do Sul"),
    RO("RO", "Rondônia"),
    RR("RR", "Roraima"),
    SC("SC", "Santa Catarina"),
    SP("SP", "São Paulo"),
    SE("SE", "Sergipe"),
    TO("TO", "Tocantins");

    private final String codigo;
    private final String nome;
    private static final Random RANDOM = new Random();

    TipoEstado(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getNomeMaiusculo() {
        return this.nome.toUpperCase();
    }

    public static TipoEstado of(String codigo) {
        return getStreamValues()
                .filter(tipoEstado -> tipoEstado.getCodigo().equals(codigo.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código inválido e/ou inexistente para o TipoEstado."));
    }

    public static TipoEstado randomTipoEstado(){
        return getStreamValues()
                .skip((int) (Math.random() * getStreamValues().count()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Não foi possível gerar um TipoEstado aleatório."));
    }

    private static Stream<TipoEstado> getStreamValues() {
        return Stream.of(TipoEstado.values());
    }
}
