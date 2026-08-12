package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions;

import org.springframework.lang.Nullable;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super();
    }

    /**
     * Constructs a new <code>EntityNotFoundException</code> exception with
     * <code>null</code> as its detail message.
     */
    public EntityNotFoundException(@Nullable Exception cause) {
        super(cause);
    }

    /**
     * Constructs a new <code>EntityNotFoundException</code> exception with the
     * specified detail message.
     *
     * @param message
     *            the detail message.
     */
    public EntityNotFoundException(@Nullable String message) {
        super(message);
    }

    /**
     * Constructs a new <code>EntityNotFoundException</code> exception with the
     * specified detail message.
     *
     * @param message
     *            the detail message.
     */
    public EntityNotFoundException(@Nullable String message, @Nullable Exception cause) {
        super(message, cause);
    }
}
