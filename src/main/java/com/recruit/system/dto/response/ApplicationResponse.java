package com.recruit.system.dto.response;

import com.recruit.system.model.ApplicationStatus;
import java.time.LocalDateTime;

public class ApplicationResponse {

    private Long id;
    private Long userId;

    private Long jobId;
    private String jobTitle;

    private ApplicationStatus status;
    private String reviewReason;
    private LocalDateTime submittedAt;

    public ApplicationResponse() {}

    public ApplicationResponse(Long id, Long userId, Long jobId, String jobTitle,
                               ApplicationStatus status, String reviewReason,
                               LocalDateTime submittedAt) {
        this.id = id;
        this.userId = userId;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.status = status;
        this.reviewReason = reviewReason;
        this.submittedAt = submittedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public String getReviewReason() {
        return reviewReason;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public void setReviewReason(String reviewReason) {
        this.reviewReason = reviewReason;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}