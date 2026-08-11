package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.InicializeComponent;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets.operations.BucketS3Creator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BucketS3Inicialize implements InicializeComponent {

    private final BucketS3Creator bucketS3InicialeCreator;
    private static final String BUCKET_NAME = "report-movimento-cliente";

    @Override
    public void inicialize() {
        log.info("Initializing Bucket S3 create");
        bucketS3InicialeCreator.createBucketIfNotExists(BUCKET_NAME);
    }
}
