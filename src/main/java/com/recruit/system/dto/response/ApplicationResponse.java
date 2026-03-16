package com.recruit.system.dto.response;

import com.recruit.system.model.ApplicationStatus;
import java.time.LocalDateTime;
import java.util.List;

public class ApplicationResponse {

    private Long id;

    private UserResponse user;   // ⭐ REPLACED userId

    private Long jobId;
    private String jobTitle;

    private ApplicationStatus status;
    private String reviewReason;
    private LocalDateTime submittedAt;

    private List<DocumentResponse> documents;

    public ApplicationResponse() {}

    public Long getId() { return id; }
    public UserResponse getUser() { return user; }
    public Long getJobId() { return jobId; }
    public String getJobTitle() { return jobTitle; }
    public ApplicationStatus getStatus() { return status; }
    public String getReviewReason() { return reviewReason; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public List<DocumentResponse> getDocuments() { return documents; }

    public void setId(Long id) { this.id = id; }
    public void setUser(UserResponse user) { this.user = user; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
    public void setReviewReason(String reviewReason) { this.reviewReason = reviewReason; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public void setDocuments(List<DocumentResponse> documents) { this.documents = documents; }
}