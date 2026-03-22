package com.devconnect.controller;

import com.devconnect.dto.NotificationResponse;
import com.devconnect.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * GET  /api/notifications          — all my notifications
 * GET  /api/notifications/unread   — only unread
 * GET  /api/notifications/count    — unread count (for bell badge)
 * PUT  /api/notifications/read-all — mark all as read
 * PUT  /api/notifications/{id}/read— mark one as read
 * DELETE /api/notifications/{id}   — delete one
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAll(
            @AuthenticationPrincipal UserDetails u) {
        return ResponseEntity.ok(notificationService.getMyNotifications(u.getUsername()));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnread(
            @AuthenticationPrincipal UserDetails u) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(u.getUsername()));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getCount(
            @AuthenticationPrincipal UserDetails u) {
        long count = notificationService.getUnreadCount(u.getUsername());
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PutMapping("/read-all")
    public ResponseEntity<String> markAllRead(
            @AuthenticationPrincipal UserDetails u) {
        notificationService.markAllRead(u.getUsername());
        return ResponseEntity.ok("All marked as read");
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<String> markRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails u) {
        notificationService.markRead(id, u.getUsername());
        return ResponseEntity.ok("Marked as read");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails u) {
        notificationService.deleteNotification(id, u.getUsername());
        return ResponseEntity.ok("Deleted");
    }
}