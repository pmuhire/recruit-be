package com.recruit.system.mapper;


import com.recruit.system.dto.request.ApplicationSubmitRequest;
import com.recruit.system.dto.response.ApplicationResponse;
import com.recruit.system.model.Application;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public Application toEntity(ApplicationSubmitRequest request) {

        Application application = new Application();

        application.setStatus("PENDING");

        return application;
    }

    public ApplicationResponse toResponse(Application application) {

        ApplicationResponse response = new ApplicationResponse();

        response.setId(application.getId());

        if(application.getApplicant() != null){
            response.setApplicantId(application.getApplicant().getId());
        }

        response.setStatus(application.getStatus());
        response.setReviewReason(application.getReviewReason());
        response.setSubmittedAt(application.getSubmittedAt());
        response.setReviewedAt(application.getReviewedAt());

        return response;
    }
}
