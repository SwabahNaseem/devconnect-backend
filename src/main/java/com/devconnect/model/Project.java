package com.devconnect.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    public enum ProjectStatus { ACTIVE, COMPLETED, DELETED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate createdAt;

    private Integer maxMembers = 4;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.ACTIVE;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_skills", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false)
    private User lead;

    @ManyToMany
    @JoinTable(
        name = "project_members",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JoinRequest> joinRequests = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectFile> files = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }

    public Project() {}

    // ── Getters ──
    public Long getId()                  { return id; }
    public String getName()              { return name; }
    public String getDescription()       { return description; }
    public LocalDate getCreatedAt()      { return createdAt; }
    public Integer getMaxMembers()       { return maxMembers == null ? 4 : maxMembers; }
    public ProjectStatus getStatus()     { return status == null ? ProjectStatus.ACTIVE : status; }
    public List<String> getSkills()      { return skills; }
    public User getLead()                { return lead; }
    public List<User> getMembers()       { return members; }
    public List<JoinRequest> getJoinRequests() { return joinRequests; }
    public List<ProjectFile> getFiles()  { return files; }
    public List<Message> getMessages()   { return messages; }

    // ── Setters ──
    public void setId(Long id)                         { this.id = id; }
    public void setName(String name)                   { this.name = name; }
    public void setDescription(String d)               { this.description = d; }
    public void setCreatedAt(LocalDate c)              { this.createdAt = c; }
    public void setMaxMembers(Integer m)               { this.maxMembers = m; }
    public void setStatus(ProjectStatus s)             { this.status = s; }
    public void setSkills(List<String> s)              { this.skills = s; }
    public void setLead(User lead)                     { this.lead = lead; }
    public void setMembers(List<User> m)               { this.members = m; }
    public void setJoinRequests(List<JoinRequest> j)   { this.joinRequests = j; }
    public void setFiles(List<ProjectFile> f)          { this.files = f; }
    public void setMessages(List<Message> m)           { this.messages = m; }

    // ── Builder ──
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String name;
        private String description;
        private List<String> skills    = new ArrayList<>();
        private User lead;
        private Integer maxMembers     = 4;
        private ProjectStatus status   = ProjectStatus.ACTIVE;
        private List<User> members     = new ArrayList<>();

        public Builder name(String v)              { this.name = v;        return this; }
        public Builder description(String v)       { this.description = v; return this; }
        public Builder skills(List<String> v)      { this.skills = v;      return this; }
        public Builder lead(User v)                { this.lead = v;        return this; }
        public Builder maxMembers(Integer v)       { this.maxMembers = v;  return this; }
        public Builder status(ProjectStatus v)     { this.status = v;      return this; }
        public Builder members(List<User> v)       { this.members = v;     return this; }

        public Project build() {
            Project p = new Project();
            p.name        = this.name;
            p.description = this.description;
            p.skills      = this.skills;
            p.lead        = this.lead;
            p.maxMembers  = this.maxMembers;
            p.status      = this.status;
            p.members     = this.members;
            return p;
        }
    }
}