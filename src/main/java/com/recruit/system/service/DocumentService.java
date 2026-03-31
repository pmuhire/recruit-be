package com.recruit.system.service;

import com.recruit.system.dto.response.DocumentResponse;
import com.recruit.system.mapper.DocumentMapper;
import com.recruit.system.model.Application;
import com.recruit.system.model.Document;
import com.recruit.system.repository.DocumentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;
    private final SupabaseStorageService storageService;
    private final DocumentMapper documentMapper;

    public DocumentService(
            DocumentRepository documentRepository,
            SupabaseStorageService storageService,
            DocumentMapper documentMapper
    ) {
        this.documentRepository = documentRepository;
        this.storageService = storageService;
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

        log.info("Uploading document for applicationId={}, originalFileName={}", application.getId(), file.getOriginalFilename());

        String filePath = storageService.uploadFile(file, application.getId());

        Document document = new Document();
        document.setApplication(application);
        document.setFileName(file.getOriginalFilename());
        document.setFileKey(filePath);
        document.setUploadedAt(LocalDateTime.now());

        Document savedDocument = documentRepository.save(document);
        log.info("Document saved with id={} and fileKey={}", savedDocument.getId(), savedDocument.getFileKey());

        DocumentResponse response = documentMapper.toResponse(savedDocument);
        response.setSuccess(true);
        response.setMessage("Document uploaded successfully");

        return response;
    }

    public String generateDocumentViewUrlById(Long documentId) {
        log.info("Generating document view URL for documentId={}", documentId);

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> {
                    log.error("Document not found for id={}", documentId);
                    return new RuntimeException("Document not found");
                });

        log.info("Found document id={}, fileName={}, fileKey={}", document.getId(), document.getFileName(), document.getFileKey());

        String url = storageService.createSignedUrl(document.getFileKey());
        log.info("Generated signed URL successfully for documentId={}", documentId);

        return url;
    }
}