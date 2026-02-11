package com.CollabIDE.operation;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public abstract class EditOperation {
    protected UUID sessionId;
    protected String filePath;
    protected String userId;
    protected long baseVersion;
    protected Instant timestamp;

    public enum Type{
        INSERT,
        DELETE
    }

    public abstract Type getType();
}
