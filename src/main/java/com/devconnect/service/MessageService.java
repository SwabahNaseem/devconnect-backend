package com.devconnect.service;

import com.devconnect.dto.*;
import com.devconnect.model.*;
import com.devconnect.repository.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    private static final String BASE_URL = "http://localhost:8080/";

    public MessageService(MessageRepository messageRepository,
                          ProjectRepository projectRepository,
                          UserRepository userRepository,
                          NotificationService notificationService) {
        this.messageRepository   = messageRepository;
        this.projectRepository   = projectRepository;
        this.userRepository      = userRepository;
        this.notificationService = notificationService;
    }

    public MessageResponse sendMessage(Long projectId, String email, MessageRequest request) {
        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        boolean isMember = false;
        for (User m : project.getMembers()) if (m.getId().equals(sender.getId())) { isMember = true; break; }
        if (!isMember) throw new RuntimeException("Only project members can send messages");

        Message message = Message.builder()
                .text(request.getText()).sender(sender).project(project).build();
        Message saved = messageRepository.save(message);

        // Notify other members about the new message
        notificationService.notifyNewMessage(sender, project);

        return mapToResponse(saved);
    }

    public List<MessageResponse> getMessages(Long projectId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        boolean isMember = false;
        for (User m : project.getMembers()) if (m.getId().equals(user.getId())) { isMember = true; break; }
        if (!isMember) throw new RuntimeException("Only project members can read messages");

        List<MessageResponse> result = new ArrayList<>();
        for (Message msg : messageRepository.findByProjectOrderBySentAtAsc(project))
            result.add(mapToResponse(msg));
        return result;
    }

    private MessageResponse mapToResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId()).text(message.getText()).sentAt(message.getSentAt())
                .senderId(message.getSender().getId()).senderName(message.getSender().getName())
                .senderProfileImageUrl(message.getSender().getProfileImagePath() != null
                        ? BASE_URL + message.getSender().getProfileImagePath() : null)
                .build();
    }
}