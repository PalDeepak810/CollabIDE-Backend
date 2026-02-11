package com.CollabIDE.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "edit_operations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditOperationEntity {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private UUID sessionId;
    private String filePath;
    private String userId;
    private long baseVersion;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType type;

    private Integer position;
    private Integer length;


    @Column(columnDefinition = "TEXT")
    private String text;
    private Instant timestamp;

    public enum OperationType{
        INSERT,
        DELETE
    }

}
