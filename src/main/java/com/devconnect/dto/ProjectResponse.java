package com.devconnect.dto;

import java.time.LocalDate;
import java.util.List;

public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDate createdAt;
    private List<String> skills;
    private UserSummary lead;
    private List<UserSummary> members;
    private Integer pendingRequestsCount;
    private Integer filesCount;
    private Integer maxMembers;
    private boolean isMember;
    private boolean hasRequested;
    private boolean isLead;

    public ProjectResponse() {}

    // ── Getters ──
    public Long getId()                        { return id; }
    public String getName()                    { return name; }
    public String getDescription()             { return description; }
    public LocalDate getCreatedAt()            { return createdAt; }
    public List<String> getSkills()            { return skills; }
    public UserSummary getLead()               { return lead; }
    public List<UserSummary> getMembers()      { return members; }
    public Integer getPendingRequestsCount()   { return pendingRequestsCount; }
    public Integer getFilesCount()             { return filesCount; }
    public Integer getMaxMembers()             { return maxMembers; }
    public boolean isMember()                  { return isMember; }
    public boolean isHasRequested()            { return hasRequested; }
    public boolean isIsLead()                  { return isLead; }

    // ── Setters ──
    public void setId(Long id)                                         { this.id = id; }
    public void setName(String name)                                   { this.name = name; }
    public void setDescription(String description)                     { this.description = description; }
    public void setCreatedAt(LocalDate createdAt)                      { this.createdAt = createdAt; }
    public void setSkills(List<String> skills)                         { this.skills = skills; }
    public void setLead(UserSummary lead)                              { this.lead = lead; }
    public void setMembers(List<UserSummary> members)                  { this.members = members; }
    public void setPendingRequestsCount(Integer pendingRequestsCount)  { this.pendingRequestsCount = pendingRequestsCount; }
    public void setFilesCount(Integer filesCount)                      { this.filesCount = filesCount; }
    public void setMaxMembers(Integer maxMembers)                      { this.maxMembers = maxMembers; }
    public void setMember(boolean isMember)                            { this.isMember = isMember; }
    public void setHasRequested(boolean hasRequested)                  { this.hasRequested = hasRequested; }
    public void setIsLead(boolean isLead)                              { this.isLead = isLead; }

    // ── Builder ──
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id;
        private String name;
        private String description;
        private LocalDate createdAt;
        private List<String> skills;
        private UserSummary lead;
        private List<UserSummary> members;
        private Integer pendingRequestsCount;
        private Integer filesCount;
        private Integer maxMembers;
        private boolean isMember;
        private boolean hasRequested;
        private boolean isLead;

        public Builder id(Long id)                               { this.id = id; return this; }
        public Builder name(String name)                         { this.name = name; return this; }
        public Builder description(String d)                     { this.description = d; return this; }
        public Builder createdAt(LocalDate c)                    { this.createdAt = c; return this; }
        public Builder skills(List<String> s)                    { this.skills = s; return this; }
        public Builder lead(UserSummary lead)                    { this.lead = lead; return this; }
        public Builder members(List<UserSummary> members)        { this.members = members; return this; }
        public Builder pendingRequestsCount(Integer c)           { this.pendingRequestsCount = c; return this; }
        public Builder filesCount(Integer c)                     { this.filesCount = c; return this; }
        public Builder maxMembers(Integer c)                     { this.maxMembers = c; return this; }
        public Builder isMember(boolean b)                       { this.isMember = b; return this; }
        public Builder hasRequested(boolean b)                   { this.hasRequested = b; return this; }
        public Builder isLead(boolean b)                         { this.isLead = b; return this; }

        public ProjectResponse build() {
            ProjectResponse r = new ProjectResponse();
            r.id = id;
            r.name = name;
            r.description = description;
            r.createdAt = createdAt;
            r.skills = skills;
            r.lead = lead;
            r.members = members;
            r.pendingRequestsCount = pendingRequestsCount;
            r.filesCount = filesCount;
            r.maxMembers = maxMembers;
            r.isMember = isMember;
            r.hasRequested = hasRequested;
            r.isLead = isLead;
            return r;
        }
    }

    // ── Inner class UserSummary ──
    public static class UserSummary {
        private Long id;
        private String name;
        private String email;
        private String profileImageUrl;
        private List<String> skills;

        public UserSummary() {}

        public Long getId()                   { return id; }
        public String getName()               { return name; }
        public String getEmail()              { return email; }
        public String getProfileImageUrl()    { return profileImageUrl; }
        public List<String> getSkills()       { return skills; }

        public void setId(Long id)                                  { this.id = id; }
        public void setName(String name)                            { this.name = name; }
        public void setEmail(String email)                          { this.email = email; }
        public void setProfileImageUrl(String profileImageUrl)      { this.profileImageUrl = profileImageUrl; }
        public void setSkills(List<String> skills)                  { this.skills = skills; }

        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private Long id;
            private String name;
            private String email;
            private String profileImageUrl;
            private List<String> skills;

            public Builder id(Long id)                      { this.id = id; return this; }
            public Builder name(String name)                { this.name = name; return this; }
            public Builder email(String email)              { this.email = email; return this; }
            public Builder profileImageUrl(String url)      { this.profileImageUrl = url; return this; }
            public Builder skills(List<String> skills)      { this.skills = skills; return this; }

            public UserSummary build() {
                UserSummary s = new UserSummary();
                s.id = id;
                s.name = name;
                s.email = email;
                s.profileImageUrl = profileImageUrl;
                s.skills = skills;
                return s;
            }
        }
    }
}