package com.devconnect.controller;

import com.devconnect.dto.MessageRequest;
import com.devconnect.dto.MessageResponse;
import com.devconnect.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(messageService.getMessages(id, userDetails.getUsername()));
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageResponse> sendMessage(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody MessageRequest request) {
        return ResponseEntity.ok(messageService.sendMessage(id, userDetails.getUsername(), request));
    }
}