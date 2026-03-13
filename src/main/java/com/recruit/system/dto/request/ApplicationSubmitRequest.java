package com.recruit.system.dto.request;

public class ApplicationSubmitRequest {

    private Long userId;
    private Long jobId;

    public ApplicationSubmitRequest() {}

    public Long getUserId() {
        return userId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}