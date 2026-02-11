package com.CollabIDE.api;

import com.CollabIDE.Services.SessionService;
import com.CollabIDE.api.dto.CreateSessionRequest;
import com.CollabIDE.session.SessionState;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public SessionState createSession(@RequestBody CreateSessionRequest request) {
        if (request == null || request.getOwnerId() == null || request.getOwnerId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ownerId is required");
        }

        return sessionService.createSession(request.getOwnerId());
    }

    @GetMapping("/{sessionId}")
    public SessionState getSession(@PathVariable UUID sessionId) {
        return sessionService.getSessionState(sessionId);
    }
}
