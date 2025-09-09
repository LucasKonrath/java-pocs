package org.ldamaceno.contract_verifier;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(
    stubsMode = StubRunnerProperties.StubsMode.LOCAL,
    ids = "org.ldamaceno:contract-verifier:+:stubs:8080"
)
public class ConsumerContractTest {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080";

    @Test
    public void should_get_user_by_id() {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
            baseUrl + "/api/users/1", String.class);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).contains("John Doe");
        assertThat(response.getBody()).contains("john.doe@example.com");
    }

    @Test
    public void should_return_404_for_non_existent_user() {
        // When & Then
        try {
            restTemplate.getForEntity(baseUrl + "/api/users/999", String.class);
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode().value()).isEqualTo(404);
        }
    }

    @Test
    public void should_create_user_successfully() {
        // Given
        String userJson = """
            {
                "name": "Alice Johnson",
                "email": "alice.johnson@example.com",
                "active": true
            }
            """;

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
            baseUrl + "/api/users", userJson, String.class);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).contains("Alice Johnson");
    }
}
