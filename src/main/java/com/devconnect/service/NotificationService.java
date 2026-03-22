package com.devconnect.service;

import com.devconnect.dto.NotificationResponse;
import com.devconnect.model.*;
import com.devconnect.repository.NotificationRepository;
import com.devconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository,
                                UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    // ── Create notifications ──────────────────────────────────

    /** Someone sent a join request to your project — notify the lead */
    public void notifyJoinRequest(User actor, User lead, Project project) {
        if (actor.getId().equals(lead.getId())) return;
        Notification n = Notification.builder()
                .type(Notification.NotifType.JOIN_REQUEST)
                .message(actor.getName() + " requested to join " + project.getName())
                .recipient(lead)
                .actor(actor)
                .project(project)
                .build();
        notificationRepository.save(n);
    }

    /** Join request was accepted — notify the user who requested */
    public void notifyRequestAccepted(User actor, User recipient, Project project) {
        Notification n = Notification.builder()
                .type(Notification.NotifType.REQUEST_ACCEPTED)
                .message("Your request to join " + project.getName() + " was accepted!")
                .recipient(recipient)
                .actor(actor)
                .project(project)
                .build();
        notificationRepository.save(n);
    }

    /** New message sent — notify all project members except sender */
    public void notifyNewMessage(User sender, Project project) {
        for (User member : project.getMembers()) {
            if (member.getId().equals(sender.getId())) continue; // don't notify sender
            Notification n = Notification.builder()
                    .type(Notification.NotifType.MESSAGE)
                    .message(sender.getName() + " sent a message in " + project.getName())
                    .recipient(member)
                    .actor(sender)
                    .project(project)
                    .build();
            notificationRepository.save(n);
        }
    }

    // ── Read notifications ────────────────────────────────────

    /** Get all notifications for current user */
    public List<NotificationResponse> getMyNotifications(String email) {
        User user = getUserByEmail(email);
        List<Notification> notifications =
                notificationRepository.findByRecipientOrderByCreatedAtDesc(user);
        List<NotificationResponse> result = new ArrayList<>();
        for (Notification n : notifications) result.add(map(n));
        return result;
    }

    /** Get only unread notifications */
    public List<NotificationResponse> getUnreadNotifications(String email) {
        User user = getUserByEmail(email);
        List<Notification> notifications =
                notificationRepository.findByRecipientAndIsReadFalseOrderByCreatedAtDesc(user);
        List<NotificationResponse> result = new ArrayList<>();
        for (Notification n : notifications) result.add(map(n));
        return result;
    }

    /** Count unread notifications */
    public long getUnreadCount(String email) {
        User user = getUserByEmail(email);
        return notificationRepository.countByRecipientAndIsReadFalse(user);
    }

    /** Mark all notifications as read */
    public void markAllRead(String email) {
        User user = getUserByEmail(email);
        notificationRepository.markAllReadForUser(user);
    }

    /** Mark single notification as read */
    public void markRead(Long notifId, String email) {
        Notification n = notificationRepository.findById(notifId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setRead(true);
        notificationRepository.save(n);
    }

    /** Delete a notification */
    public void deleteNotification(Long notifId, String email) {
        Notification n = notificationRepository.findById(notifId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        if (!n.getRecipient().getEmail().equals(email)) {
            throw new RuntimeException("Not your notification");
        }
        notificationRepository.delete(n);
    }

    // ── Helper ───────────────────────────────────────────────

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private NotificationResponse map(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .type(n.getType())
                .message(n.getMessage())
                .isRead(n.isRead())
                .createdAt(n.getCreatedAt())
                .projectId(n.getProject() != null ? n.getProject().getId() : null)
                .projectName(n.getProject() != null ? n.getProject().getName() : null)
                .actorId(n.getActor() != null ? n.getActor().getId() : null)
                .actorName(n.getActor() != null ? n.getActor().getName() : null)
                .build();
    }
}