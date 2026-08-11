package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database;

public final class DynamoDbConstants {

    private DynamoDbConstants() {
        throw new IllegalStateException("This is a utility class DynamoDbConstants and cannot be instantiated");
    }

    public static final String TABLE_NAME_EMPRESTIMOS_CLIENTES = "emprestimos_cliente";
    public static final String TABLE_NAME_HISTORICO_CLIENTE = "historico_movimento_cliente";

    public static final String DEFAUL_KEY_CLIENTE_CODIGO = "cod_idef_cliente";

    public static final String DEFAUL_KEY_DATA_CRIACAO = "data_criacao";

}
