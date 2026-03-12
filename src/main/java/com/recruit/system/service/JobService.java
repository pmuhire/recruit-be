package com.recruit.system.service;

import com.recruit.system.dto.request.JobRequest;
import com.recruit.system.dto.response.JobResponse;
import com.recruit.system.model.Job;
import com.recruit.system.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    // Create job
    public JobResponse createJob(JobRequest request) {
        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());
        job.setStatus(Job.Status.OPEN);

        Job saved = jobRepository.save(job);

        return mapToResponse(saved);
    }

    // Get all jobs
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Helper to map entity to DTO
    private JobResponse mapToResponse(Job job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setRequirements(job.getRequirements());
        response.setStatus(job.getStatus());
        response.setCreatedAt(job.getCreatedAt());
        return response;
    }
}