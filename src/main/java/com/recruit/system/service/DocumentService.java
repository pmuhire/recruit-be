package com.recruit.system.service;

import com.recruit.system.dto.response.DocumentResponse;
import com.recruit.system.mapper.DocumentMapper;
import com.recruit.system.model.Application;
import com.recruit.system.model.Document;
import com.recruit.system.repository.DocumentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final S3Service s3Service;
    private final DocumentMapper documentMapper;

    public DocumentService(
            DocumentRepository documentRepository,
            S3Service s3Service,
            DocumentMapper documentMapper
    ) {
        this.documentRepository = documentRepository;
        this.s3Service = s3Service;
        this.documentMapper = documentMapper;
    }

    @Transactional
    public DocumentResponse uploadDocumentForApplication(Application application, MultipartFile file) {
        if (application == null) {
            throw new IllegalArgumentException("Application must not be null");
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        String storedKey = s3Service.uploadFile(file, application.getId());

        Document document = new Document();
        document.setApplication(application);
        document.setFileName(file.getOriginalFilename());
        document.setFileUrl(storedKey); // stores S3 key, not public URL
        document.setUploadedAt(LocalDateTime.now());

        Document savedDocument = documentRepository.save(document);

        application.getDocuments().add(savedDocument);

        DocumentResponse response = documentMapper.toResponse(savedDocument);
        response.setSuccess(true);
        response.setMessage("Document uploaded successfully");

        return response;
    }

    public DocumentResponse getDocumentResponse(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("Document must not be null");
        }
        return documentMapper.toResponse(document);
    }

    public String generateDocumentViewUrl(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("Document must not be null");
        }

        String key = s3Service.extractKey(document.getFileUrl());
        return s3Service.generatePresignedUrl(key);
    }
}