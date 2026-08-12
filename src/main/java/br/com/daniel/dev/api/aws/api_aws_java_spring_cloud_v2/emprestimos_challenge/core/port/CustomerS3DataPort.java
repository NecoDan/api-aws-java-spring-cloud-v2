package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.emprestimos_challenge.core.port;

import org.springframework.web.multipart.MultipartFile;

public interface CustomerS3DataPort {

    void enviarDadosBucket(String bucketName, String key, MultipartFile file);

    void enviarDadosParaBucket(String bucketName, String fileName, byte[] data);
}
