package com.recruit.system.mapper;
import com.recruit.system.dto.response.DocumentResponse;
import com.recruit.system.model.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public DocumentResponse toResponse(Document document) {

        DocumentResponse response = new DocumentResponse();

        response.setId(document.getId());
        response.setFileName(document.getFileName());
        response.setFileUrl(document.getFileUrl());
        response.setUploadedAt(document.getUploadedAt());

        return response;
    }
}