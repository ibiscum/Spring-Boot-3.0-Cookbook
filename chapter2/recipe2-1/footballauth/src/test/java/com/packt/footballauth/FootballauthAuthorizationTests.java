package com.packt.footballauth;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FootballauthAuthorizationTests {

    @LocalServerPort
    private int port;

    private final RestClient restClient = RestClient.create();

    @Test
    void clientCredentialsTokenRequestReturnsAccessToken() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "football:read");
        body.add("client_id", "football");
        body.add("client_secret", "SuperSecret");

        // Typsichere Auswertung über ParameterizedTypeReference
        ResponseEntity<Map<String, Object>> response = restClient.post()
                .uri("http://localhost:" + port + "/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {});

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        Map<String, Object> responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody).containsKeys("access_token", "token_type");
        assertThat(responseBody.get("token_type")).isEqualTo("Bearer");
    }

    @Test
    void clientCredentialsWithInvalidSecretIsUnauthorized() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "football:read");
        body.add("client_id", "football");
        body.add("client_secret", "bad-secret");

        // Wir erwarten explizit die HttpClientErrorException.Unauthorized (401)
        HttpClientErrorException.Unauthorized exception = assertThrows(
            HttpClientErrorException.Unauthorized.class,
            () -> restClient.post()
                    .uri("http://localhost:" + port + "/oauth2/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
        );

        // Assertions auf der abgefangenen Exception ausführen
        assertThat(exception.getStatusCode().is4xxClientError()).isTrue();
        assertThat(exception.getResponseBodyAsString()).contains("invalid_client");
    }
}

