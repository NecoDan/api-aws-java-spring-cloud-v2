package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.database;

public final class DynamoDbConstants {

    private DynamoDbConstants() {
        throw new IllegalStateException("This is a utility class DynamoDbConstants and cannot be instantiated");
    }

    public static final String TABLE_NAME_EMPRESTIMOS_CLIENTES = "emprestimos_cliente";
}
