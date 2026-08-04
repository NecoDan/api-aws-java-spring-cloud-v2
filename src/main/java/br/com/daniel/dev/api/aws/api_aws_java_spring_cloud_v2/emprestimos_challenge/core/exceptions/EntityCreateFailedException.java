package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.exceptions;

import org.springframework.lang.Nullable;

public class EntityCreateFailedException extends RuntimeException {

    /**
     * Constructs a new <code>EntityCreateFailedException</code> exception with
     * <code>null</code> as its detail message.
     */
    public EntityCreateFailedException() {
        super();
    }

    /**
     * Constructs a new <code>EntityCreateFailedException</code> exception with
     * <code>null</code> as its detail message.
     */
    public EntityCreateFailedException(@Nullable Exception cause) {
        super(cause);
    }

    /**
     * Constructs a new <code>EntityCreateFailedException</code> exception with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public EntityCreateFailedException(@Nullable String message) {
        super(message);
    }

    /**
     * Constructs a new <code>EntityCreateFailedException</code> exception with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public EntityCreateFailedException(@Nullable String message, @Nullable Exception cause) {
        super(message, cause);
    }
}
