package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Classe utilitária com múltiplas funções utilitárias.
 * <p>
 * <p>
 * Esta classe fornece métodos para geração
 *
 * <p>Esta classe não pode ser instanciada.</p>
 */
public final class FunctionalUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String BR_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    private static final Locale PT_BR = new Locale.Builder().setLanguage("pt").setRegion("BR").build();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private FunctionalUtils() {
        throw new IllegalStateException("This is a utility class FunctionalUtils and cannot be instantiated");
    }

    /**
     * Converte uma lista de mapas simplificados para uma string JSON usando GSON.
     *
     * @param simplifiedItems A lista de mapas contendo os itens a serem convertidos.
     * @return Uma string JSON representando os itens fornecidos.
     */
    public static String toStringJsonFromGSONBy(List<Map<String, Object>> simplifiedItems) {
        return GSON.toJson(simplifiedItems);
    }

    /**
     * Converte uma classe para uma string JSON.
     *
     * @param clazz A classe a ser convertida.
     * @return Uma string JSON representando a classe fornecida.
     */
    public static String toStringJson(Class<?> clazz) {
        try {
            return MAPPER.writeValueAsString(clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(String.format("Failed create e/or convert to JSON string object from value: %s", e.getMessage()));
        }
    }

    /**
     * Converte um objeto para uma string JSON.
     *
     * @param object O objeto a ser convertido.
     * @return Uma string JSON representando o objeto fornecido.
     */
    public static String toStringJsonFrom(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(String.format("Failed create e/or convert to JSON string object from value: %s", e.getMessage()));
        }
    }


    public static String formatCreationDate(LocalDateTime localDateTime) {
        return (Objects.isNull(localDateTime)) ? StringUtils.EMPTY : formatCreationDateBy(localDateTime);
    }

    /**
     * Formata uma data e hora no padrão brasileiro.
     *
     * @param localDateTime A data e hora a serem formatadas.
     * @return Uma string formatada no padrão "dd/MM/yyyy HH:mm:ss".
     */
    public static String formatCreationDateBy(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(BR_DATETIME_FORMAT));
    }

    /**
     * Formata um número decimal para duas casas decimais.
     *
     * @param value O valor decimal a ser formatado.
     * @return Uma string representando o número formatado.
     */
    public static String formatDecimalNumberBy(Double value) {
        return formatDecimalNumber(BigDecimal.valueOf(value));
    }

    /**
     * Formata um número decimal para duas casas decimais.
     *
     * @param value O valor decimal em formato BigDecimal a ser formatado.
     * @return Uma string representando o número formatado.
     */
    public static String formatDecimalNumber(BigDecimal value) {
        validateValorNumericoFormatCasasDecimais(value);
        value = value.setScale(2, RoundingMode.HALF_UP);

        var symbols = new DecimalFormatSymbols(PT_BR);
        symbols.setDecimalSeparator('.');

        var format = new DecimalFormat("##0.00", symbols);
        return format.format(value);
    }

    /**
     * Formata um CPF no padrão brasileiro.
     *
     * @param cpf O CPF a ser formatado (apenas números).
     * @return Uma string representando o CPF formatado no padrão "XXX.XXX.XXX-XX".
     */
    public static String formatCpf(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", ""); // Remover caracteres não numéricos
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private static void validateValorNumericoFormatCasasDecimais(BigDecimal number) {
        if (Objects.isNull(number))
            throw new IllegalArgumentException("Valor numerico encontra-se inválido e/ou inexsitente.");
    }
}
