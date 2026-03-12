package com.recruit.system.service;

import com.recruit.system.dto.request.ApplicantCreateRequest;
import com.recruit.system.dto.response.ApplicantResponse;
import com.recruit.system.mapper.ApplicantMapper;
import com.recruit.system.model.Applicant;
import com.recruit.system.repository.ApplicantRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final ApplicantMapper applicantMapper;

    // ✅ Constructor injection
    public ApplicantService(ApplicantRepository applicantRepository, ApplicantMapper applicantMapper) {
        this.applicantRepository = applicantRepository;
        this.applicantMapper = applicantMapper;
    }

    public ApplicantResponse createApplicant(ApplicantCreateRequest request) {
        // Map request to entity
        Applicant applicant = applicantMapper.toEntity(request);

        // Save entity
        applicant = applicantRepository.save(applicant);

        // Map entity to response
        return applicantMapper.toResponse(applicant);
    }
}