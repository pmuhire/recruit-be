package com.recruit.system.mapper;
import com.recruit.system.dto.request.ApplicantCreateRequest;
import com.recruit.system.dto.response.ApplicantResponse;
import com.recruit.system.model.Applicant;
import org.springframework.stereotype.Component;

@Component
public class ApplicantMapper {

    public Applicant toEntity(ApplicantCreateRequest request) {

        Applicant applicant = new Applicant();

        applicant.setNidNumber(request.getNidNumber());
        applicant.setFirstName(request.getFirstName());
        applicant.setLastName(request.getLastName());
        applicant.setPhone(request.getPhone());
        applicant.setEmail(request.getEmail());
        applicant.setDateOfBirth(request.getDateOfBirth());

        return applicant;
    }

    public ApplicantResponse toResponse(Applicant applicant) {

        ApplicantResponse response = new ApplicantResponse();

        response.setId(applicant.getId());
        response.setNidNumber(applicant.getNidNumber());
        response.setFirstName(applicant.getFirstName());
        response.setLastName(applicant.getLastName());
        response.setPhone(applicant.getPhone());
        response.setEmail(applicant.getEmail());
        response.setDateOfBirth(applicant.getDateOfBirth());

        return response;
    }
}