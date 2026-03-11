package com.recruit.system.service;

import com.recruit.system.dto.request.ApplicantCreateRequest;
import com.recruit.system.dto.response.ApplicantResponse;
import com.recruit.system.mapper.ApplicantMapper;
import com.recruit.system.model.Applicant;
import com.recruit.system.repository.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final ApplicantMapper applicantMapper;

    public ApplicantResponse createApplicant(ApplicantCreateRequest request){

        Applicant applicant = applicantMapper.toEntity(request);

        applicant = applicantRepository.save(applicant);

        return applicantMapper.toResponse(applicant);
    }

}