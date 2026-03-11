package com.recruit.system.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationResponse {

    private Long id;

    private Long applicantId;

    private String status;

    private String reviewReason;

    private LocalDateTime submittedAt;

    private LocalDateTime reviewedAt;

}