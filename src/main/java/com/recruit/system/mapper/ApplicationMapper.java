package com.recruit.system.mapper;

import com.recruit.system.dto.response.ApplicationResponse;
import com.recruit.system.dto.response.DocumentResponse;
import com.recruit.system.dto.response.UserResponse;
import com.recruit.system.model.Application;
import com.recruit.system.model.Users;
import com.recruit.system.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApplicationMapper {

    private final UserRepository userRepository;
    private final DocumentMapper documentMapper;

    public ApplicationMapper(UserRepository userRepository, DocumentMapper documentMapper) {
        this.userRepository = userRepository;
        this.documentMapper = documentMapper;
    }

    public ApplicationResponse toResponse(Application application) {
        ApplicationResponse response = new ApplicationResponse();

        response.setId(application.getId());

        Users user = userRepository.findById(application.getUserId()).orElse(null);
        if (user != null) {
            UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole()
            );
            response.setUser(userResponse);
        }

        if (application.getJob() != null) {
            response.setJobId(application.getJob().getId());
            response.setJobTitle(application.getJob().getTitle());
        }

        response.setStatus(application.getStatus());
        response.setReviewReason(application.getReviewReason());
        response.setSubmittedAt(application.getSubmittedAt());

        List<DocumentResponse> documentResponses =
                application.getDocuments() == null
                        ? Collections.emptyList()
                        : application.getDocuments()
                        .stream()
                        .map(documentMapper::toResponse)
                        .collect(Collectors.toList());

        response.setDocuments(documentResponses);

        return response;
    }
}