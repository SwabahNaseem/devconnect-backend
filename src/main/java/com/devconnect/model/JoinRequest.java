package com.devconnect.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "join_requests")
public class JoinRequest {

    public enum RequestStatus {
        PENDING, ACCEPTED, DECLINED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    private LocalDateTime requestedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @PrePersist
    public void prePersist() {
        this.requestedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = RequestStatus.PENDING;
        }
    }

    // ── No-arg constructor ──
    public JoinRequest() {}

    // ── Getters ──
    public Long getId() { return id; }
    public RequestStatus getStatus() { return status; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public User getUser() { return user; }
    public Project getProject() { return project; }

    // ── Setters ──
    public void setId(Long id) { this.id = id; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
    public void setUser(User user) { this.user = user; }
    public void setProject(Project project) { this.project = project; }

    // ── Builder ──
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private RequestStatus status;
        private User user;
        private Project project;

        public Builder status(RequestStatus status) { this.status = status; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder project(Project project) { this.project = project; return this; }

        public JoinRequest build() {
            JoinRequest jr = new JoinRequest();
            jr.status = this.status;
            jr.user = this.user;
            jr.project = this.project;
            return jr;
        }
    }
}