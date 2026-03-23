package com.devconnect.controller;

import com.devconnect.dto.MessageRequest;
import com.devconnect.dto.MessageResponse;
import com.devconnect.service.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

/**
 * Handles real-time WebSocket chat messages.
 * Client sends to:   /app/chat/{projectId}
 * Server broadcasts: /topic/chat/{projectId}
 */
@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    public ChatController(SimpMessagingTemplate messagingTemplate,
                          MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService    = messageService;
    }

    @MessageMapping("/chat/{projectId}")
    public void sendMessage(@DestinationVariable Long projectId,
                            @Payload MessageRequest request,
                            @AuthenticationPrincipal UserDetails userDetails) {
        // Save to DB and notify via REST notifications
        MessageResponse saved = messageService.sendMessage(projectId, userDetails.getUsername(), request);
        // Broadcast to all subscribers of this project's chat topic
        messagingTemplate.convertAndSend("/topic/chat/" + projectId, saved);
    }
}