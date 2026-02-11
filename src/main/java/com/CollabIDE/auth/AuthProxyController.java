package com.CollabIDE.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthProxyController {

    private final AuthServiceClient authServiceClient;

    public AuthProxyController(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, Object> body) {
        return authServiceClient.proxyLogin(body);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, Object> body) {
        return authServiceClient.proxyRegister(body);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(authServiceClient.me(token));
    }
}
