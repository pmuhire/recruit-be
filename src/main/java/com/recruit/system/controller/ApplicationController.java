package com.recruit.system.controller;

import com.recruit.system.dto.request.ApplicationSubmitRequest;
import com.recruit.system.dto.response.ApiResponse;
import com.recruit.system.dto.response.ApplicationResponse;
import com.recruit.system.model.ApplicationStatus;
import com.recruit.system.service.ApplicationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    // Applicant applies to a job
    @PreAuthorize("hasAuthority('APPLICANT')")
    @PostMapping
    public ApiResponse<ApplicationResponse> apply(@RequestBody ApplicationSubmitRequest request) {
        ApplicationResponse response = service.apply(request);
        return new ApiResponse<>(true, "Application submitted successfully", response);
    }

    // HR/SUPERADMIN approve an application
    @PreAuthorize("hasAnyAuthority('HR','SUPERADMIN')")
    @PatchMapping("/{id}/approve")
    public ApiResponse<ApplicationResponse> approveApplication(
            @PathVariable Long id,
            @RequestParam(required = false) String reviewReason) {

        ApplicationResponse response = service.reviewApplication(id, ApplicationStatus.APPROVED, reviewReason);
        return new ApiResponse<>(true, "Application approved", response);
    }

    // HR/SUPERADMIN reject an application
    @PreAuthorize("hasAnyAuthority('HR','SUPERADMIN')")
    @PatchMapping("/{id}/reject")
    public ApiResponse<ApplicationResponse> rejectApplication(
            @PathVariable Long id,
            @RequestParam(required = false) String reviewReason) {

        ApplicationResponse response = service.reviewApplication(id, ApplicationStatus.REJECTED, reviewReason);
        return new ApiResponse<>(true, "Application rejected", response);
    }

    // HR/SUPERADMIN: Get all applications
     @PreAuthorize("hasAnyAuthority('HR','SUPERADMIN')")
    @GetMapping
    public ApiResponse<List<ApplicationResponse>> getAllApplications() {
        List<ApplicationResponse> applications = service.getAllApplications();
        return new ApiResponse<>(true, "Applications retrieved successfully", applications);
    }

    // Applicant: Get own applications
    @PreAuthorize("hasAuthority('APPLICANT')")
    @GetMapping("/my")
    public ApiResponse<List<ApplicationResponse>> getMyApplications(@RequestParam Long userId) {
        List<ApplicationResponse> applications = service.getApplicationsByUser(userId);
        return new ApiResponse<>(true, "Your applications retrieved successfully", applications);
    }

    // HR/SUPERADMIN: Get applications by job
    @PreAuthorize("hasAnyAuthority('HR','SUPERADMIN')")
    @GetMapping("/job/{jobId}")
    public ApiResponse<List<ApplicationResponse>> getApplicationsByJob(@PathVariable Long jobId) {
        List<ApplicationResponse> applications = service.getApplicationsByJob(jobId);
        return new ApiResponse<>(true, "Applications retrieved for job", applications);
    }
}
