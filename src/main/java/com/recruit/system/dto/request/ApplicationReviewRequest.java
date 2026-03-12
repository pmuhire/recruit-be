package com.recruit.system.dto.request;

public class ApplicationReviewRequest {

    private String status;
    private String reviewReason;

    // Default constructor
    public ApplicationReviewRequest() {
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReviewReason() {
        return reviewReason;
    }

    public void setReviewReason(String reviewReason) {
        this.reviewReason = reviewReason;
    }
}