package com.CollabIDE.websocket.controller;

import com.CollabIDE.Services.FileStateService;
import com.CollabIDE.filestate.FileState;
import com.CollabIDE.operation.DeleteOperation;
import com.CollabIDE.operation.EditOperation;
import com.CollabIDE.operation.InsertOperation;
import com.CollabIDE.websocket.dto.EditOperationMessage;
import com.CollabIDE.websocket.dto.FileUpdateMessage;
import com.CollabIDE.Services.SessionService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class EditOperationWebSocketController {
    private final FileStateService fileStateService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SessionService sessionService;

    public EditOperationWebSocketController(FileStateService fileStateService, SimpMessagingTemplate messagingTemplate, SessionService sessionService) {
        this.fileStateService = fileStateService;
        this.messagingTemplate = messagingTemplate;
        this.sessionService = sessionService;
    }



    @MessageMapping("/edit")
    public void handleEdit(EditOperationMessage message){
        if (message.getUserId() == null || !sessionService.isParticipant(message.getSessionId(), message.getUserId())) {
            java.util.Map<String, Object> err = new java.util.HashMap<>();
            err.put("type", "ERROR");
            err.put("payload", "Not a session participant");
            messagingTemplate.convertAndSend("/topic/session/" + message.getSessionId(), err);
            return;
        }

        //1.Convert DTO->Domain Operation
        EditOperation operation=toDomainOperation(message);

        //2.Apply operation(OT+DB inside service)
        FileState updated=fileStateService.applyOperation(operation);

        //3.Broadcast updated file state (or better, broadcast the rebased operation - for now let's broadcast full update to maintain frontend compatibility until we update it)
        FileUpdateMessage broadcast=new FileUpdateMessage();
        broadcast.setSessionId(updated.getSessionId());
        broadcast.setFilePath(updated.getFilePath());
        broadcast.setContent(updated.getContent());
        broadcast.setVersion(updated.getVersion());

        messagingTemplate.convertAndSend(
                "/topic/session/"+updated.getSessionId(),
                broadcast
        );
    }


    //DTO->Domain

    private EditOperation toDomainOperation(EditOperationMessage msg){
        EditOperation op;

        if(msg.getType()==EditOperationMessage.OperationType.INSERT){
            InsertOperation insert=new InsertOperation();
            insert.setPosition(msg.getPosition());
            insert.setText(msg.getText());
            op=insert;
        }else{
            DeleteOperation delete=new DeleteOperation();
            delete.setPosition(msg.getPosition());
            delete.setLength(msg.getLength());
            op=delete;
        }
        op.setSessionId(msg.getSessionId());
        op.setFilePath(msg.getFilePath());
        op.setUserId(msg.getUserId());
        op.setBaseVersion(msg.getBaseVersion());
        op.setTimestamp(
                msg.getTimestamp()!=null? msg.getTimestamp(): Instant.now()
        );
        return op;
    }
}
