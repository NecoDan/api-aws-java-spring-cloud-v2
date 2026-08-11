package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.aws.controller;

import br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.config.SecretsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.ListSecretsResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretListEntry;

import java.util.Properties;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SecretsManagerControllerTest {

    @Mock
    private SecretsConfig secretsConfig;

    @Mock
    private SecretsManagerClient secretsManagerClient;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SecretsManagerController(secretsConfig)).build();
        when(secretsConfig.getSecretsManagerClient()).thenReturn(secretsManagerClient);
    }

    @Test
    void shouldCreateSecretValue() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("db-password", "s3cr3t");

        try (MockedStatic<SecretsConfig> mocked = mockStatic(SecretsConfig.class)) {
            mocked.when(() -> SecretsConfig.createSecretsFromAWS(
                    secretsManagerClient,
                    "db-password",
                    "s3cr3t",
                    "database password"
            )).thenReturn(properties);

            mockMvc.perform(post("/secrets/v2/configuration/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"secretName\":\"db-password\",\"secretValue\":\"s3cr3t\",\"description\":\"database password\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.secret_name").value("s3cr3t"));
        }
    }

    @Test
    void shouldGetSecretValueById() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("db-password", "s3cr3t");

        try (MockedStatic<SecretsConfig> mocked = mockStatic(SecretsConfig.class)) {
            mocked.when(() -> SecretsConfig.getSecretsFromAWS(secretsManagerClient, "db-password"))
                    .thenReturn(properties);

            mockMvc.perform(get("/secrets/v2/configuration/{secret_name_id}", "db-password"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.secret_name").value("db-password"))
                    .andExpect(jsonPath("$.secret_value").value("s3cr3t"));
        }
    }

    @Test
    void shouldListAllSecrets() throws Exception {
        ListSecretsResponse response = ListSecretsResponse.builder()
                .secretList(java.util.List.of(
                        SecretListEntry.builder()
                                .name("db-password")
                                .description("database password")
                                .arn("arn:aws:secretsmanager:sa-east-1:123456789012:secret:db-password")
                                .build()))
                .build();

        try (MockedStatic<SecretsConfig> mocked = mockStatic(SecretsConfig.class)) {
            mocked.when(() -> SecretsConfig.getAllSecretsFromAWS(secretsManagerClient))
                    .thenReturn(response);

            mockMvc.perform(get("/secrets/v2/configuration"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].secret_name").value("db-password"))
                    .andExpect(jsonPath("$[0].description").value("database password"))
                    .andExpect(jsonPath("$[0].arn").value("arn:aws:secretsmanager:sa-east-1:123456789012:secret:db-password"));
        }
    }
}
