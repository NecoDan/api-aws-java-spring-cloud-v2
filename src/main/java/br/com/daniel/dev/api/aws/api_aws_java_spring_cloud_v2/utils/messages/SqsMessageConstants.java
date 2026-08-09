package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages;

public class SqsMessageConstants {
    private SqsMessageConstants() {
        throw new IllegalStateException("This is a utility class SqsMessageConstants and cannot be instantiated");
    }

    public static final String MESSAGE_RECEIVED = "Message received from SQS: {}";
    public static final String MESSAGE_PROCESSED_SUCCESSFULLY = "Message processed successfully: {}";
    public static final String MESSAGE_PROCESSING_FAILED = "Failed to process message: {}";
    public static final String MESSAGE_DELETED_SUCCESSFULLY = "Message deleted successfully from SQS: {}";
    public static final String MESSAGE_DELETION_FAILED = "Failed to delete message from SQS: {}";


}
