package com.devconnect.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    public enum NotifType {
        JOIN_REQUEST,    // someone requested to join your project
        MESSAGE,         // someone sent a message in your project
        REQUEST_ACCEPTED // your join request was accepted
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotifType type;

    private String message;   // human readable text
    private boolean isRead;
    private LocalDateTime createdAt;

    // Who receives this notification
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private User recipient;

    // Which project this is about
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    // Who triggered it (the sender/requester)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")
    private User actor;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    public Notification() {}

    // Getters
    public Long getId() { return id; }
    public NotifType getType() { return type; }
    public String getMessage() { return message; }
    public boolean isRead() { return isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public User getRecipient() { return recipient; }
    public Project getProject() { return project; }
    public User getActor() { return actor; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setType(NotifType type) { this.type = type; }
    public void setMessage(String message) { this.message = message; }
    public void setRead(boolean read) { isRead = read; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setRecipient(User recipient) { this.recipient = recipient; }
    public void setProject(Project project) { this.project = project; }
    public void setActor(User actor) { this.actor = actor; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private NotifType type;
        private String message;
        private User recipient, actor;
        private Project project;

        public Builder type(NotifType t)     { this.type = t; return this; }
        public Builder message(String m)     { this.message = m; return this; }
        public Builder recipient(User u)     { this.recipient = u; return this; }
        public Builder actor(User u)         { this.actor = u; return this; }
        public Builder project(Project p)    { this.project = p; return this; }

        public Notification build() {
            Notification n = new Notification();
            n.type = type; n.message = message;
            n.recipient = recipient; n.actor = actor; n.project = project;
            return n;
        }
    }
}