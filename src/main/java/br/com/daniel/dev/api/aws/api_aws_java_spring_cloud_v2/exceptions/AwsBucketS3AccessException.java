package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions;

import org.springframework.lang.Nullable;

public class AwsBucketS3AccessException extends RuntimeException {

    /**
     * Constructs a new <code>AwsBucketS3AccessException</code> exception with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public AwsBucketS3AccessException(@Nullable String message) {
        super(message);
    }

    /**
     * Constructs a new <code>AwsBucketS3AccessException</code> exception with
     * <code>null</code> as its detail message.
     */
    public AwsBucketS3AccessException(@Nullable Exception cause) {
        super(cause);
    }

    /**
     * Constructs a new <code>AwsBucketS3AccessException</code> exception with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public AwsBucketS3AccessException(@Nullable String message, @Nullable Exception cause) {
        super(message, cause);
    }
}
