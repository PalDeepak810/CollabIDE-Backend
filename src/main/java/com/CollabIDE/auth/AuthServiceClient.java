package com.CollabIDE.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.util.Map;

@Service
public class AuthServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AuthServiceClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${app.auth.base-url}") String baseUrl
    ) {
        this.restTemplate = restTemplateBuilder.build();
        this.baseUrl = baseUrl;
    }

    public Map<String, Object> me(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                baseUrl + "/auth/me",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }

    public boolean validateToken(String token) {
        try {
            Map<String, Object> me = me(token);
            return me != null && me.get("userId") != null;
        } catch (RestClientException ex) {
            return false;
        }
    }

    public ResponseEntity<String> proxyLogin(Map<String, Object> body) {
        return proxyPost("/auth/login", body);
    }

    public ResponseEntity<String> proxyRegister(Map<String, Object> body) {
        return proxyPost("/auth/register", body);
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
