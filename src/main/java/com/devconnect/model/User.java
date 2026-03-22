package com.devconnect.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String bio;
    private Integer age;
    private String linkedin;
    private String github;
    private String profileImagePath;

    // ─── Email Verification Fields ───────────────────────────
    // Whether user has verified their email
    private boolean emailVerified = false;

    // The 6-digit code we send to their email
    private String verificationCode;

    // When the code expires (10 minutes from creation)
    private Long verificationCodeExpiry;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_skills", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @ManyToMany(mappedBy = "members")
    private List<Project> memberProjects = new ArrayList<>();

    @OneToMany(mappedBy = "lead")
    private List<Project> ledProjects = new ArrayList<>();

    public User() {}

    // ── Getters ──
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getBio() { return bio; }
    public Integer getAge() { return age; }
    public String getLinkedin() { return linkedin; }
    public String getGithub() { return github; }
    public String getProfileImagePath() { return profileImagePath; }
    public boolean isEmailVerified() { return emailVerified; }
    public String getVerificationCode() { return verificationCode; }
    public Long getVerificationCodeExpiry() { return verificationCodeExpiry; }
    public List<String> getSkills() { return skills; }
    public List<Project> getMemberProjects() { return memberProjects; }
    public List<Project> getLedProjects() { return ledProjects; }

    // ── Setters ──
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setBio(String bio) { this.bio = bio; }
    public void setAge(Integer age) { this.age = age; }
    public void setLinkedin(String linkedin) { this.linkedin = linkedin; }
    public void setGithub(String github) { this.github = github; }
    public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }
    public void setVerificationCodeExpiry(Long verificationCodeExpiry) { this.verificationCodeExpiry = verificationCodeExpiry; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public void setMemberProjects(List<Project> memberProjects) { this.memberProjects = memberProjects; }
    public void setLedProjects(List<Project> ledProjects) { this.ledProjects = ledProjects; }

    // ── Builder ──
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String name, email, password, bio, linkedin, github, profileImagePath;
        private Integer age;
        private List<String> skills = new ArrayList<>();

        public Builder name(String v)              { this.name = v; return this; }
        public Builder email(String v)             { this.email = v; return this; }
        public Builder password(String v)          { this.password = v; return this; }
        public Builder bio(String v)               { this.bio = v; return this; }
        public Builder age(Integer v)              { this.age = v; return this; }
        public Builder linkedin(String v)          { this.linkedin = v; return this; }
        public Builder github(String v)            { this.github = v; return this; }
        public Builder profileImagePath(String v)  { this.profileImagePath = v; return this; }
        public Builder skills(List<String> v)      { this.skills = v; return this; }

        public User build() {
            User u = new User();
            u.name = name; u.email = email; u.password = password;
            u.bio = bio; u.age = age; u.linkedin = linkedin;
            u.github = github; u.profileImagePath = profileImagePath;
            u.skills = skills;
            return u;
        }
    }
}