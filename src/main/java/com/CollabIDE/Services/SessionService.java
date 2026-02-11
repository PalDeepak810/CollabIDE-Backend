package com.CollabIDE.Services;

import com.CollabIDE.Mapper.SessionMapper;
import com.CollabIDE.Mapper.SessionParticipantMapper;
import com.CollabIDE.session.Session;
import com.CollabIDE.session.SessionParticipant;
import com.CollabIDE.session.SessionState;
import com.CollabIDE.Repositories.SessionParticipantRepository;
import com.CollabIDE.Repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionParticipantRepository sessionParticipantRepository;

    public SessionService(SessionRepository sessionRepository, SessionParticipantRepository sessionParticipantRepository) {
        this.sessionRepository = sessionRepository;
        this.sessionParticipantRepository = sessionParticipantRepository;
    }

    public SessionState createSession(String ownerUserId) {

        UUID sessionId = UUID.randomUUID();
        Instant now = Instant.now();

        Session session = Session.builder()
                .sessionId(sessionId)
                .ownerUserId(ownerUserId)
                .createdAt(now)
                .build();

        SessionParticipant owner = SessionParticipant.builder()
                .sessionId(sessionId)
                .userID(ownerUserId)
                .role(SessionParticipant.Role.OWNER)
                .joinedAt(now)
                .lastActiveAT(now)
                .build();

        sessionRepository.save(SessionMapper.toEntity(session));
        sessionParticipantRepository.save(SessionParticipantMapper.toEntity(owner));

        return assembleSessionState(sessionId);
    }


    public SessionState joinSession(UUID sessionId, String userId, String tabId) {

        if (sessionRepository.findById(sessionId).isEmpty()) {
            Session session = Session.builder()
                    .sessionId(sessionId)
                    .ownerUserId(userId)
                    .createdAt(Instant.now())
                    .build();
            sessionRepository.save(SessionMapper.toEntity(session));

            SessionParticipant owner = SessionParticipant.builder()
                    .sessionId(sessionId)
                    .userID(userId)
                    .tabId(tabId)
                    .role(SessionParticipant.Role.OWNER)
                    .joinedAt(Instant.now())
                    .lastActiveAT(Instant.now())
                    .build();
            sessionParticipantRepository.save(SessionParticipantMapper.toEntity(owner));

            return assembleSessionState(sessionId);
        }

        boolean exists = sessionParticipantRepository
                .findBySessionId(sessionId)
                .stream()
                .anyMatch(p -> p.getUserId().equals(userId) &&
                        ((p.getTabId() == null && tabId == null) ||
                                (p.getTabId() != null && p.getTabId().equals(tabId))));

        if (!exists) {
            SessionParticipant participant = SessionParticipant.builder()
                    .sessionId(sessionId)
                    .userID(userId)
                    .tabId(tabId)
                    .role(SessionParticipant.Role.COLLABORATOR)
                    .joinedAt(Instant.now())
                    .lastActiveAT(Instant.now())
                    .build();

            sessionParticipantRepository.save(
                    SessionParticipantMapper.toEntity(participant)
            );
        }

        return assembleSessionState(sessionId);
    }


    private SessionState assembleSessionState(UUID sessionId) {

        Session session = sessionRepository.findById(sessionId)
                .map(SessionMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        List<SessionParticipant> participants =
                sessionParticipantRepository.findBySessionId(sessionId)
                        .stream()
                        .map(SessionParticipantMapper::toDomain)
                        .toList();

        return SessionState.builder()
                .session(session)
                .participants(participants)
                .build();
    }


    public SessionState getSessionState(UUID sessionId) {
        return assembleSessionState(sessionId);
    }

    public boolean isParticipant(UUID sessionId, String userId) {
        return sessionParticipantRepository
                .findBySessionId(sessionId)
                .stream()
                .anyMatch(p -> p.getUserId().equals(userId));
    }

    public boolean isOwner(UUID sessionId, String userId) {
        return sessionRepository.findById(sessionId)
                .map(s -> s.getOwnerUserId().equals(userId))
                .orElse(false);
    }

}
