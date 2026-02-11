package com.CollabIDE.Mapper;

import com.CollabIDE.Entities.SessionParticipantEntity;
import com.CollabIDE.session.SessionParticipant;

public class SessionParticipantMapper {
    public static SessionParticipant toDomain(SessionParticipantEntity entity) {
        return SessionParticipant.builder()
                .sessionId(entity.getSessionId())
                .userID(entity.getUserId())
                .tabId(entity.getTabId())
                .role(SessionParticipant.Role.valueOf(entity.getRole().name()))
                .joinedAt(entity.getJoinedAt())
                .lastActiveAT(entity.getLastActiveAt())
                .build();
    }

    public static SessionParticipantEntity toEntity(SessionParticipant domain) {
        return SessionParticipantEntity.builder()
                .sessionId(domain.getSessionId())
                .userId(domain.getUserID())
                .tabId(domain.getTabId())
                .role(SessionParticipantEntity.Role.valueOf(domain.getRole().name()))
                .joinedAt(domain.getJoinedAt())
                .lastActiveAt(domain.getLastActiveAT())
                .build();
    }
}
