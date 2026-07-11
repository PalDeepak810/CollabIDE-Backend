package com.CollabIDE.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;

@Service
public class AuthServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AuthServiceClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${app.auth.base-url}") String baseUrl
    ) {
        this.restTemplate = restTemplateBuilder
                .connectTimeout(Duration.ofSeconds(3))
                .readTimeout(Duration.ofSeconds(3))
                .build();
        this.baseUrl = baseUrl;
    }

    public Map<String, Object> me(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                baseUrl + "/api/v1/auth/me",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }

    public boolean validateToken(String token) {
        try {
            Map<String, Object> me = me(token);
            return me != null && (me.get("id") != null || me.get("userId") != null);
        } catch (Exception ex) {
            return false;
        }
    }

    public ResponseEntity<String> proxyLogin(Map<String, Object> body) {
        return proxyPost("/api/v1/auth/login", body);
    }

    public ResponseEntity<String> proxyRegister(Map<String, Object> body) {
        return proxyPost("/api/v1/auth/register", body);
    }

    private ResponseEntity<String> proxyPost(String path, Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(
                baseUrl + path,
                HttpMethod.POST,
                entity,
                String.class
        );
    }
}
