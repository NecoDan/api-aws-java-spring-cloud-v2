package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.enums;

public enum OrderStatus implements EnumStatusDesignExtensible {

    CREATED(true),
    PAID(true),
    CANCELLED(false);

    private final boolean finalStatus;

    OrderStatus(boolean finalStatus) {
        this.finalStatus = finalStatus;
    }

    @Override
    public boolean isFinal() {
        return finalStatus;
    }
}
