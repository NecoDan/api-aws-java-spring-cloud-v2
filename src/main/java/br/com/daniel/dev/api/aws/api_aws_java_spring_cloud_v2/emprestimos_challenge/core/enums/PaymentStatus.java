package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.enums;

public enum PaymentStatus implements EnumStatusDesignExtensible {

    PENDING(false),
    APPROVED(true),
    REJECTED(true);

    private final boolean finalStatus;

    PaymentStatus(boolean finalStatus) {
        this.finalStatus = finalStatus;
    }

    @Override
    public boolean isFinal() {
        return finalStatus;
    }
}
