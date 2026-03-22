package com.devconnect.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_files")
public class ProjectFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @PrePersist
    public void prePersist() {
        this.uploadedAt = LocalDateTime.now();
    }

    // ── No-arg constructor ──
    public ProjectFile() {}

    // ── Getters ──
    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public Long getFileSize() { return fileSize; }
    public String getFileType() { return fileType; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public User getUploadedBy() { return uploadedBy; }
    public Project getProject() { return project; }

    // ── Setters ──
    public void setId(Long id) { this.id = id; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    public void setUploadedBy(User uploadedBy) { this.uploadedBy = uploadedBy; }
    public void setProject(Project project) { this.project = project; }

    // ── Builder ──
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String fileName;
        private String filePath;
        private Long fileSize;
        private String fileType;
        private User uploadedBy;
        private Project project;

        public Builder fileName(String fileName) { this.fileName = fileName; return this; }
        public Builder filePath(String filePath) { this.filePath = filePath; return this; }
        public Builder fileSize(Long fileSize) { this.fileSize = fileSize; return this; }
        public Builder fileType(String fileType) { this.fileType = fileType; return this; }
        public Builder uploadedBy(User uploadedBy) { this.uploadedBy = uploadedBy; return this; }
        public Builder project(Project project) { this.project = project; return this; }

        public ProjectFile build() {
            ProjectFile pf = new ProjectFile();
            pf.fileName = this.fileName;
            pf.filePath = this.filePath;
            pf.fileSize = this.fileSize;
            pf.fileType = this.fileType;
            pf.uploadedBy = this.uploadedBy;
            pf.project = this.project;
            return pf;
        }
    }
}