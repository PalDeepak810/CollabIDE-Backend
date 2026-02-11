package com.CollabIDE.Mapper;

import com.CollabIDE.Entities.SessionEntity;
import com.CollabIDE.session.Session;

public class SessionMapper {
    public static Session toDomain(SessionEntity entity) {
        return Session.builder()
                .sessionId(entity.getSessionId())
                .ownerUserId(entity.getOwnerUserId())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static SessionEntity toEntity(Session domain) {
        return SessionEntity.builder()
                .sessionId(domain.getSessionId())
                .ownerUserId(domain.getOwnerUserId())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
