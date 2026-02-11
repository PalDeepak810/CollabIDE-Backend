package com.CollabIDE.Mapper;

import com.CollabIDE.Entities.EditOperationEntity;
import com.CollabIDE.operation.DeleteOperation;
import com.CollabIDE.operation.EditOperation;
import com.CollabIDE.operation.InsertOperation;

public class EditOperationMapper {

    // ============================
    // DOMAIN → ENTITY
    // ============================
    public static EditOperationEntity toEntity(EditOperation op) {
        EditOperationEntity entity = new EditOperationEntity();

        entity.setSessionId(op.getSessionId());
        entity.setFilePath(op.getFilePath());
        entity.setUserId(op.getUserId());
        entity.setBaseVersion(op.getBaseVersion());
        entity.setTimestamp(op.getTimestamp());

        // 🔥 FIX: enum → enum
        entity.setType(
                EditOperationEntity.OperationType.valueOf(op.getType().name())
        );


        if (op instanceof InsertOperation insert) {
            entity.setPosition(insert.getPosition());
            entity.setText(insert.getText());
            entity.setLength(null);
        }

        if (op instanceof DeleteOperation delete) {
            entity.setPosition(delete.getPosition());
            entity.setLength(delete.getLength());
            entity.setText(null);
        }

        return entity;
    }


    // ============================
    // ENTITY → DOMAIN (NULL SAFE)
    // ============================
    public static EditOperation toDomain(EditOperationEntity entity) {

        EditOperation op;

        switch (entity.getType()) {

            case INSERT -> {
                InsertOperation insert = new InsertOperation();
                insert.setPosition(entity.getPosition());
                insert.setText(entity.getText());
                op = insert;
            }

            case DELETE -> {
                DeleteOperation delete = new DeleteOperation();
                delete.setPosition(entity.getPosition());
                delete.setLength(entity.getLength());
                op = delete;
            }

            default -> throw new IllegalStateException(
                    "Unknown operation type: " + entity.getType()
            );
        }

        // common fields
        op.setSessionId(entity.getSessionId());
        op.setFilePath(entity.getFilePath());
        op.setUserId(entity.getUserId());
        op.setBaseVersion(entity.getBaseVersion());
        op.setTimestamp(entity.getTimestamp());

        return op;
    }

}
