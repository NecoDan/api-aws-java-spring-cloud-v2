package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public final class FunctionalUtils {
    private FunctionalUtils() {
        throw new IllegalStateException("This is a utility class FunctionalUtils and cannot be instantiated");
    }

    private static final String BR_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    private static final Locale PT_BR = new Locale.Builder().setLanguage("pt").setRegion("BR").build();

    public static String formatCreationDate(LocalDateTime localDateTime) {
        return (Objects.isNull(localDateTime)) ? StringUtils.EMPTY : formatCreationDateBy(localDateTime);
    }

    public static String formatCreationDateBy(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(BR_DATETIME_FORMAT));
    }

    public static String formatDecimalNumberBy(Double value) {
        return formatDecimalNumber(BigDecimal.valueOf(value));
    }

    public static String formatDecimalNumber(BigDecimal value) {
        validateValorNumericoFormatCasasDecimais(value);
        value = value.setScale(2, RoundingMode.HALF_UP);

        var symbols = new DecimalFormatSymbols(PT_BR);
        symbols.setDecimalSeparator('.');

        var format = new DecimalFormat("##0.00", symbols);
        return format.format(value);
    }

    public static String formatCpf(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", ""); // Remover caracteres não numéricos
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private static void validateValorNumericoFormatCasasDecimais(BigDecimal number) {
        if (Objects.isNull(number))
            throw new IllegalArgumentException("Valor numerico encontra-se inválido e/ou inexsitente.");
    }
}
