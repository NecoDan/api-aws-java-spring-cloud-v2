package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.controller;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.FunctionalUtils;
import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils.messages.operations.MessageSqsOperations;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SqsControllerTest {

    @Mock
    private MessageSqsOperations sqsOperations;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SqsController(sqsOperations)).build();
    }

    @Test
    void shouldReturnPayloadAsJsonString() throws Exception {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("name", "Ana");
        payload.put("amount", 10);

        mockMvc.perform(post("/sqs/v1/payload_text_from_jsonbody")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string(FunctionalUtils.toStringJsonFrom(payload)));
    }

    @Test
    void shouldPublishMessageToQueue() throws Exception {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("customer", "Ana");
        payload.put("amount", 1000);
        String expectedJson = FunctionalUtils.toStringJsonFrom(payload);

        mockMvc.perform(post("/sqs/v1/publish_message")
                        .header("queue_name", "customer-queue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Mensagem enviada com sucesso para fila SQS: customer-queue"));

        verify(sqsOperations).sendMessage("customer-queue", expectedJson);
    }
}
