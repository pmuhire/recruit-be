package com.recruit.system.dto.request;

public class DocumentUploadRequest {

    private Long applicationId;

    // Default constructor
    public DocumentUploadRequest() {
    }

    // Getter and Setter
    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }
}