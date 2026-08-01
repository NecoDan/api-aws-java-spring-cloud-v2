package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.exceptions;

import java.io.Serial;

public class LoanNotAvailableException extends RuntimeException {
    @Serial private static final long serialVersionUID = -7389408912521414111L;

    public LoanNotAvailableException(String s) {
    }
}
