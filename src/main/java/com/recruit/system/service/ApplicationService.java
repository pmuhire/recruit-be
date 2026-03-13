package com.recruit.system.service;

import com.recruit.system.dto.request.ApplicationSubmitRequest;
import com.recruit.system.dto.response.ApplicationResponse;
import com.recruit.system.mapper.ApplicationMapper;
import com.recruit.system.model.Application;
import com.recruit.system.model.ApplicationStatus;
import com.recruit.system.model.Job;
import com.recruit.system.repository.ApplicationRepository;
import com.recruit.system.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final ApplicationMapper mapper;

    public ApplicationService(ApplicationRepository applicationRepository,
                              JobRepository jobRepository,
                              ApplicationMapper mapper) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.mapper = mapper;
    }

    public ApplicationResponse apply(ApplicationSubmitRequest request) {

        // Prevent duplicate application
        if(applicationRepository.existsByUserIdAndJobId(
                request.getUserId(),
                request.getJobId())) {

            throw new IllegalArgumentException("You have already applied to this job");
        }

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        Application application = mapper.toEntity(request, job);

        Application saved = applicationRepository.save(application);

        return mapper.toResponse(saved);
    }

    public List<ApplicationResponse> getAllApplications() {

        return applicationRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ApplicationResponse> getApplicationsByUser(Long userId) {

        return applicationRepository.findByUserId(userId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
    public ApplicationResponse reviewApplication(Long applicationId, ApplicationStatus status, String reviewReason) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        application.setStatus(status);
        application.setReviewReason(reviewReason);

        Application saved = applicationRepository.save(application);

        return mapper.toResponse(saved);
    }

    public List<ApplicationResponse> getApplicationsByJob(Long jobId) {

        return applicationRepository.findByJobId(jobId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}