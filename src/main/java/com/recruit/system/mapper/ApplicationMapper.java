package com.recruit.system.mapper;

import com.recruit.system.dto.request.ApplicationSubmitRequest;
import com.recruit.system.dto.response.ApplicationResponse;
import com.recruit.system.model.Application;
import com.recruit.system.model.ApplicationStatus;
import com.recruit.system.model.Job;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ApplicationMapper {

    public Application toEntity(ApplicationSubmitRequest request, Job job) {

        Application application = new Application();

        application.setUserId(request.getUserId());
        application.setJob(job);
        application.setStatus(ApplicationStatus.PENDING);
        application.setSubmittedAt(LocalDateTime.now());

        return application;
    }

    public ApplicationResponse toResponse(Application application) {

        ApplicationResponse response = new ApplicationResponse();

        response.setId(application.getId());
        response.setUserId(application.getUserId());

        if(application.getJob() != null){
            response.setJobId(application.getJob().getId());
            response.setJobTitle(application.getJob().getTitle());
        }

        response.setStatus(application.getStatus());
        response.setReviewReason(application.getReviewReason());
        response.setSubmittedAt(application.getSubmittedAt());

        return response;
    }
}