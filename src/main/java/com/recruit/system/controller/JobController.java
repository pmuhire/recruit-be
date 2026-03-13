package com.recruit.system.controller;

import com.recruit.system.dto.request.JobRequest;
import com.recruit.system.dto.response.ApiResponse;
import com.recruit.system.dto.response.JobResponse;
import com.recruit.system.service.JobService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // HR/SUPERADMIN: create job
    @PreAuthorize("hasAnyAuthority('HR','SUPERADMIN')")
    @PostMapping("/create")
    public ApiResponse<JobResponse> createJob(@RequestBody JobRequest request) {
        JobResponse response = jobService.createJob(request);
        return new ApiResponse<>(true, "Job created successfully", response);
    }

    // Get all jobs (public)
    @GetMapping("/all")
    public ApiResponse<List<JobResponse>> getAllJobs() {
        List<JobResponse> jobs = jobService.getAllJobs();
        return new ApiResponse<>(true, "Jobs retrieved successfully", jobs);
    }

    // Get single job (public)
    @GetMapping("/{jobId}")
    public ApiResponse<JobResponse> getJob(@PathVariable Long jobId) {
        JobResponse response = jobService.getJob(jobId);
        return new ApiResponse<>(true, "Job retrieved successfully", response);
    }

    // HR/SUPERADMIN: close job
    @PreAuthorize("hasAnyAuthority('HR','SUPERADMIN')")
    @PatchMapping("/{jobId}/close")
    public ApiResponse<JobResponse> closeJob(@PathVariable Long jobId) {
        JobResponse response = jobService.closeJob(jobId);
        return new ApiResponse<>(true, "Job closed successfully", response);
    }
}