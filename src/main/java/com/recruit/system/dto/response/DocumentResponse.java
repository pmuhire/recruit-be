package com.recruit.system.dto.response;

import java.time.LocalDateTime;

public class DocumentResponse {

    private boolean success;
    private String message;

    private Long id;
    private String fileName;
    private String fileUrl;
    private LocalDateTime uploadedAt;

    public DocumentResponse() {}

    public DocumentResponse(boolean success, String message, Long id, String fileName, String fileUrl, LocalDateTime uploadedAt) {
        this.success = success;
        this.message = message;
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.uploadedAt = uploadedAt;
    }

    // Success / message
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    // Document data
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}