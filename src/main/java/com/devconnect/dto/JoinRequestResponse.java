package com.devconnect.dto;

import com.devconnect.model.JoinRequest;
import java.time.LocalDateTime;
import java.util.List;

public class JoinRequestResponse {
    private Long id;
    private JoinRequest.RequestStatus status;
    private LocalDateTime requestedAt;
    private Long userId;
    private String userName;
    private String userEmail;
    private String userProfileImageUrl;
    private List<String> userSkills;

    public JoinRequestResponse() {}

    public Long getId() { return id; }
    public JoinRequest.RequestStatus getStatus() { return status; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getUserEmail() { return userEmail; }
    public String getUserProfileImageUrl() { return userProfileImageUrl; }
    public List<String> getUserSkills() { return userSkills; }

    public void setId(Long id) { this.id = id; }
    public void setStatus(JoinRequest.RequestStatus status) { this.status = status; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public void setUserProfileImageUrl(String url) { this.userProfileImageUrl = url; }
    public void setUserSkills(List<String> userSkills) { this.userSkills = userSkills; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private JoinRequest.RequestStatus status;
        private LocalDateTime requestedAt; private Long userId;
        private String userName; private String userEmail;
        private String userProfileImageUrl; private List<String> userSkills;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder status(JoinRequest.RequestStatus status) { this.status = status; return this; }
        public Builder requestedAt(LocalDateTime r) { this.requestedAt = r; return this; }
        public Builder userId(Long userId) { this.userId = userId; return this; }
        public Builder userName(String userName) { this.userName = userName; return this; }
        public Builder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public Builder userProfileImageUrl(String url) { this.userProfileImageUrl = url; return this; }
        public Builder userSkills(List<String> skills) { this.userSkills = skills; return this; }

        public JoinRequestResponse build() {
            JoinRequestResponse r = new JoinRequestResponse();
            r.id = id; r.status = status; r.requestedAt = requestedAt;
            r.userId = userId; r.userName = userName; r.userEmail = userEmail;
            r.userProfileImageUrl = userProfileImageUrl; r.userSkills = userSkills;
            return r;
        }
    }
}