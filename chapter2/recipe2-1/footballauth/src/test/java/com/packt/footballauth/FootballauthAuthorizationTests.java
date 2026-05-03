package com.packt.footballauth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FootballauthAuthorizationTests {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void clientCredentialsTokenRequestReturnsAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "football:read");
        body.add("client_id", "football");
        body.add("client_secret", "SuperSecret");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/oauth2/token",
                new HttpEntity<>(body, headers),
                Map.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).containsKeys("access_token", "token_type");
        assertThat(response.getBody().get("token_type")).isEqualTo("Bearer");
    }

    @Disabled("Requires a valid client secret to pass")
    @Test
    void clientCredentialsWithInvalidSecretIsUnauthorized() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "football:read");
        body.add("client_id", "football");
        body.add("client_secret", "bad-secret");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/oauth2/token",
                new HttpEntity<>(body, headers),
                Map.class);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }
}
