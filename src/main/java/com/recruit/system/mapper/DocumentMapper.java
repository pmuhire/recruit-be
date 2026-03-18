package com.recruit.system.mapper;

import com.recruit.system.dto.response.DocumentResponse;
import com.recruit.system.model.Document;
import com.recruit.system.service.S3Service;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    private final S3Service s3Service;

    public DocumentMapper(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public DocumentResponse toResponse(Document document) {
        DocumentResponse response = new DocumentResponse();

        response.setId(document.getId());
        response.setFileName(document.getFileName());

        String key = s3Service.extractKey(document.getFileUrl());
        response.setFileUrl(s3Service.generatePresignedUrl(key));

        response.setUploadedAt(document.getUploadedAt());

        return response;
    }
}