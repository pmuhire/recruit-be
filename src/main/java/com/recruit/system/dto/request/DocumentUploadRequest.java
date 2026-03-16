package com.recruit.system.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class DocumentUploadRequest {

    private Long applicationId;
    private MultipartFile file;

    public DocumentUploadRequest() {}

    public DocumentUploadRequest(Long applicationId, MultipartFile file) {
        this.applicationId = applicationId;
        this.file = file;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}