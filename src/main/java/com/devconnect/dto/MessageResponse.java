package com.devconnect.dto;

import java.time.LocalDateTime;

public class MessageResponse {
    private Long id;
    private String text;
    private LocalDateTime sentAt;
    private Long senderId;
    private String senderName;
    private String senderProfileImageUrl;

    public MessageResponse() {}

    public Long getId() { return id; }
    public String getText() { return text; }
    public LocalDateTime getSentAt() { return sentAt; }
    public Long getSenderId() { return senderId; }
    public String getSenderName() { return senderName; }
    public String getSenderProfileImageUrl() { return senderProfileImageUrl; }

    public void setId(Long id) { this.id = id; }
    public void setText(String text) { this.text = text; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public void setSenderProfileImageUrl(String url) { this.senderProfileImageUrl = url; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private String text; private LocalDateTime sentAt;
        private Long senderId; private String senderName; private String senderProfileImageUrl;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder text(String text) { this.text = text; return this; }
        public Builder sentAt(LocalDateTime sentAt) { this.sentAt = sentAt; return this; }
        public Builder senderId(Long senderId) { this.senderId = senderId; return this; }
        public Builder senderName(String senderName) { this.senderName = senderName; return this; }
        public Builder senderProfileImageUrl(String url) { this.senderProfileImageUrl = url; return this; }

        public MessageResponse build() {
            MessageResponse r = new MessageResponse();
            r.id = id; r.text = text; r.sentAt = sentAt;
            r.senderId = senderId; r.senderName = senderName;
            r.senderProfileImageUrl = senderProfileImageUrl;
            return r;
        }
    }
}