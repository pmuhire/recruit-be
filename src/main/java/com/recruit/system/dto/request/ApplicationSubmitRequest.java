package com.recruit.system.dto.request;

public class ApplicationSubmitRequest {

    private Long applicantId;

    // Default constructor
    public ApplicationSubmitRequest() {
    }

    // Getter and Setter
    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }
}