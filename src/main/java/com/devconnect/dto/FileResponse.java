package com.devconnect.dto;

import java.time.LocalDateTime;

public class FileResponse {
    private Long id;
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private String fileType;
    private LocalDateTime uploadedAt;
    private String uploadedByName;
    private Long uploadedById;

    public FileResponse() {}

    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public String getFileUrl() { return fileUrl; }
    public Long getFileSize() { return fileSize; }
    public String getFileType() { return fileType; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public String getUploadedByName() { return uploadedByName; }
    public Long getUploadedById() { return uploadedById; }

    public void setId(Long id) { this.id = id; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    public void setUploadedByName(String uploadedByName) { this.uploadedByName = uploadedByName; }
    public void setUploadedById(Long uploadedById) { this.uploadedById = uploadedById; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private String fileName; private String fileUrl;
        private Long fileSize; private String fileType; private LocalDateTime uploadedAt;
        private String uploadedByName; private Long uploadedById;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder fileName(String fileName) { this.fileName = fileName; return this; }
        public Builder fileUrl(String fileUrl) { this.fileUrl = fileUrl; return this; }
        public Builder fileSize(Long fileSize) { this.fileSize = fileSize; return this; }
        public Builder fileType(String fileType) { this.fileType = fileType; return this; }
        public Builder uploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; return this; }
        public Builder uploadedByName(String name) { this.uploadedByName = name; return this; }
        public Builder uploadedById(Long id) { this.uploadedById = id; return this; }

        public FileResponse build() {
            FileResponse r = new FileResponse();
            r.id = id; r.fileName = fileName; r.fileUrl = fileUrl;
            r.fileSize = fileSize; r.fileType = fileType; r.uploadedAt = uploadedAt;
            r.uploadedByName = uploadedByName; r.uploadedById = uploadedById;
            return r;
        }
    }
}