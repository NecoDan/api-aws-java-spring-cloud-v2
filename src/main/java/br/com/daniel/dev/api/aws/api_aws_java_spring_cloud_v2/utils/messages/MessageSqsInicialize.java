package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.InicializeComponent;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages.operations.MessageSqsCreatorQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageSqsInicialize implements InicializeComponent {

    private final MessageSqsCreatorQueue messageSqsCreatorQueue;

    @Override
    public void inicialize() {

    }
}
