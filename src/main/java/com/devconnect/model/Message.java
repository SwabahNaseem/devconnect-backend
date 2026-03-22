package com.devconnect.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    private LocalDateTime sentAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @PrePersist
    public void prePersist() {
        this.sentAt = LocalDateTime.now();
    }

    // ── No-arg constructor ──
    public Message() {}

    // ── Getters ──
    public Long getId() { return id; }
    public String getText() { return text; }
    public LocalDateTime getSentAt() { return sentAt; }
    public User getSender() { return sender; }
    public Project getProject() { return project; }

    // ── Setters ──
    public void setId(Long id) { this.id = id; }
    public void setText(String text) { this.text = text; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    public void setSender(User sender) { this.sender = sender; }
    public void setProject(Project project) { this.project = project; }

    // ── Builder ──
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String text;
        private User sender;
        private Project project;

        public Builder text(String text) { this.text = text; return this; }
        public Builder sender(User sender) { this.sender = sender; return this; }
        public Builder project(Project project) { this.project = project; return this; }

        public Message build() {
            Message m = new Message();
            m.text = this.text;
            m.sender = this.sender;
            m.project = this.project;
            return m;
        }
    }
}