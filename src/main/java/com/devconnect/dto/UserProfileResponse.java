package com.devconnect.dto;

import java.util.List;

public class UserProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private Integer age;
    private String linkedin;
    private String github;
    private String profileImageUrl;
    private List<String> skills;

    public UserProfileResponse() {}

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getBio() { return bio; }
    public Integer getAge() { return age; }
    public String getLinkedin() { return linkedin; }
    public String getGithub() { return github; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public List<String> getSkills() { return skills; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setBio(String bio) { this.bio = bio; }
    public void setAge(Integer age) { this.age = age; }
    public void setLinkedin(String linkedin) { this.linkedin = linkedin; }
    public void setGithub(String github) { this.github = github; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private String name; private String email;
        private String bio; private Integer age; private String linkedin;
        private String github; private String profileImageUrl; private List<String> skills;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder bio(String bio) { this.bio = bio; return this; }
        public Builder age(Integer age) { this.age = age; return this; }
        public Builder linkedin(String linkedin) { this.linkedin = linkedin; return this; }
        public Builder github(String github) { this.github = github; return this; }
        public Builder profileImageUrl(String url) { this.profileImageUrl = url; return this; }
        public Builder skills(List<String> skills) { this.skills = skills; return this; }

        public UserProfileResponse build() {
            UserProfileResponse r = new UserProfileResponse();
            r.id = id; r.name = name; r.email = email; r.bio = bio;
            r.age = age; r.linkedin = linkedin; r.github = github;
            r.profileImageUrl = profileImageUrl; r.skills = skills;
            return r;
        }
    }
}