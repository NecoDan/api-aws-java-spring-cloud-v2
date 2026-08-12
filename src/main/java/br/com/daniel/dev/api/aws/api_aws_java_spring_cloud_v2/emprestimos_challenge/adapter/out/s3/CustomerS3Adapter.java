package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.adapter.out.s3;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.port.CustomerS3DataPort;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.exceptions.AwsBucketS3AccessException;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.buckets.operations.BucketS3Operations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerS3Adapter implements CustomerS3DataPort {

    private final BucketS3Operations bucketS3Operations;

    @Override
    public void enviarDadosBucket(String bucketName,
                                  String key,
                                  MultipartFile file) {
        try {
            log.info("Iniciando o envio de dados para o bucket S3. Bucket: {}, Key: {}", bucketName, key);
            bucketS3Operations.uploadFileBy(bucketName, key, file);
            log.info("Dados enviados com sucesso para o bucket S3. Bucket: {}, Key: {}", bucketName, key);
        } catch (Exception e) {
            log.error("Falha ao enviar dados para o bucket S3. Bucket: {}, Key: {}. Detalhes do erro: {}",
                    bucketName, key, e.getMessage(), e);

            throw new AwsBucketS3AccessException(
                    "Falha ao enviar dados para o bucket S3. Bucket: %s, Key: %s. Detalhes do erro: %s"
                            .formatted(bucketName, key, e.getMessage()), e
            );
        }
    }

    @Override
    public void enviarDadosParaBucket(String bucketName, String fileName, byte[] data) {

    }
}
