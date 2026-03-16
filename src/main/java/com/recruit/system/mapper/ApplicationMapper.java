package com.recruit.system.mapper;

import com.recruit.system.dto.response.*;
import com.recruit.system.model.Application;
import com.recruit.system.model.Document;
import com.recruit.system.model.Users;
import com.recruit.system.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApplicationMapper {

    private final UserRepository userRepository;

    public ApplicationMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ApplicationResponse toResponse(Application application) {

        ApplicationResponse response = new ApplicationResponse();

        response.setId(application.getId());

        // ⭐ Fetch user
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

        // Documents
        List<DocumentResponse> docs = application.getDocuments()
                .stream()
                .map(this::mapDocument)
                .collect(Collectors.toList());

        response.setDocuments(docs);

        return response;
    }

    private DocumentResponse mapDocument(Document document) {

        DocumentResponse res = new DocumentResponse();

        res.setId(document.getId());
        res.setFileName(document.getFileName());
        res.setFileUrl(document.getFileUrl());
        res.setUploadedAt(document.getUploadedAt());

        res.setSuccess(true);
        res.setMessage("Document retrieved");

        return res;
    }
}