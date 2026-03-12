package com.recruit.system.controller;

import com.recruit.system.dto.request.JobRequest;
import com.recruit.system.dto.response.JobResponse;
import com.recruit.system.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // HR creates a job
    @PostMapping("/create")
    public ResponseEntity<JobResponse> createJob(@RequestBody JobRequest request) {
        JobResponse response = jobService.createJob(request);
        return ResponseEntity.ok(response);
    }

    // Get all jobs
    @GetMapping("/all")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<JobResponse> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }
}