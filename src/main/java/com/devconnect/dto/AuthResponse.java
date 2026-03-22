package com.devconnect.dto;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String name;
    private String email;
    private String profileImagePath;

    public AuthResponse() {}

    // Getters
    public String getToken() { return token; }
    public String getType() { return type; }
    public Long getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getProfileImagePath() { return profileImagePath; }

    // Setters
    public void setToken(String token) { this.token = token; }
    public void setType(String type) { this.type = type; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String token;
        private Long userId;
        private String name;
        private String email;
        private String profileImagePath;

        public Builder token(String token) { this.token = token; return this; }
        public Builder userId(Long userId) { this.userId = userId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder profileImagePath(String p) { this.profileImagePath = p; return this; }

        public AuthResponse build() {
            AuthResponse r = new AuthResponse();
            r.token = this.token;
            r.userId = this.userId;
            r.name = this.name;
            r.email = this.email;
            r.profileImagePath = this.profileImagePath;
            return r;
        }
    }
}