package com.devconnect.dto;

import com.devconnect.model.Notification;
import java.time.LocalDateTime;

public class NotificationResponse {
    private Long id;
    private Notification.NotifType type;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
    private Long projectId;
    private String projectName;
    private Long actorId;
    private String actorName;

    public NotificationResponse() {}

    public Long getId() { return id; }
    public Notification.NotifType getType() { return type; }
    public String getMessage() { return message; }
    public boolean isRead() { return isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getProjectId() { return projectId; }
    public String getProjectName() { return projectName; }
    public Long getActorId() { return actorId; }
    public String getActorName() { return actorName; }

    public void setId(Long id) { this.id = id; }
    public void setType(Notification.NotifType type) { this.type = type; }
    public void setMessage(String message) { this.message = message; }
    public void setRead(boolean read) { isRead = read; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public void setActorId(Long actorId) { this.actorId = actorId; }
    public void setActorName(String actorName) { this.actorName = actorName; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private Notification.NotifType type; private String message;
        private boolean isRead; private LocalDateTime createdAt;
        private Long projectId; private String projectName;
        private Long actorId; private String actorName;

        public Builder id(Long v)            { this.id = v; return this; }
        public Builder type(Notification.NotifType v) { this.type = v; return this; }
        public Builder message(String v)     { this.message = v; return this; }
        public Builder isRead(boolean v)     { this.isRead = v; return this; }
        public Builder createdAt(LocalDateTime v) { this.createdAt = v; return this; }
        public Builder projectId(Long v)     { this.projectId = v; return this; }
        public Builder projectName(String v) { this.projectName = v; return this; }
        public Builder actorId(Long v)       { this.actorId = v; return this; }
        public Builder actorName(String v)   { this.actorName = v; return this; }

        public NotificationResponse build() {
            NotificationResponse r = new NotificationResponse();
            r.id = id; r.type = type; r.message = message; r.isRead = isRead;
            r.createdAt = createdAt; r.projectId = projectId; r.projectName = projectName;
            r.actorId = actorId; r.actorName = actorName;
            return r;
        }
    }
}