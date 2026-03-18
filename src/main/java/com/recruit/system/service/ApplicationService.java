package com.recruit.system.service;

import com.recruit.system.dto.request.ApplicationSubmitRequest;
import com.recruit.system.dto.response.ApplicationResponse;
import com.recruit.system.dto.response.DocumentResponse;
import com.recruit.system.mapper.ApplicationMapper;
import com.recruit.system.model.Application;
import com.recruit.system.model.ApplicationStatus;
import com.recruit.system.model.Document;
import com.recruit.system.model.Job;
import com.recruit.system.repository.ApplicationRepository;
import com.recruit.system.repository.JobRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final ApplicationMapper mapper;
    private final DocumentService documentService;
    public ApplicationService(ApplicationRepository applicationRepository,
                              JobRepository jobRepository,
                              ApplicationMapper mapper,
                              DocumentService documentService) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.mapper = mapper;
        this.documentService = documentService;
    }
    @Transactional
    public ApplicationResponse apply(Long userId, Long jobId, MultipartFile cvFile) {

        if (applicationRepository.existsByUserIdAndJobId(userId, jobId)) {
            throw new IllegalArgumentException("You have already applied to this job");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        Application application = new Application();
        application.setUserId(userId);
        application.setJob(job);
        application.setStatus(ApplicationStatus.PENDING);
        application.setSubmittedAt(LocalDateTime.now());

        application = applicationRepository.save(application);

        DocumentResponse docResponse =
                documentService.uploadDocumentForApplication(application, cvFile);

        if (!docResponse.isSuccess()) {
            throw new RuntimeException("Failed to upload document");
        }

        return mapper.toResponse(application);
    }
    public List<ApplicationResponse> getAllApplications() {

        return applicationRepository.findAllWithDocuments()
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