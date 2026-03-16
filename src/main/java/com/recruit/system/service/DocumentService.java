package com.recruit.system.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.recruit.system.dto.response.DocumentResponse;
import com.recruit.system.model.Application;
import com.recruit.system.model.Document;
import com.recruit.system.repository.ApplicationRepository;
import com.recruit.system.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class DocumentService {

    private final Cloudinary cloudinary;
    private final DocumentRepository documentRepository;
    private final ApplicationRepository applicationRepository;

    public DocumentService(Cloudinary cloudinary,
                           DocumentRepository documentRepository,
                           ApplicationRepository applicationRepository) {
        this.cloudinary = cloudinary;
        this.documentRepository = documentRepository;
        this.applicationRepository = applicationRepository;
    }

    public DocumentResponse uploadDocument(Long applicationId, MultipartFile file) {
        Optional<Application> appOpt = applicationRepository.findById(applicationId);
        if (appOpt.isEmpty()) {
            return new DocumentResponse(false, "Application not found", null, null, null, null);
        }

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "applications/" + applicationId
            ));

            String fileUrl = (String) uploadResult.get("secure_url");

            Document doc = new Document();
            doc.setApplication(appOpt.get());
            doc.setFileName(file.getOriginalFilename());
            doc.setFileUrl(fileUrl);
            doc.setUploadedAt(LocalDateTime.now());

            Document saved = documentRepository.save(doc);

            return new DocumentResponse(
                    true,
                    "File uploaded successfully",
                    saved.getId(),
                    saved.getFileName(),
                    saved.getFileUrl(),
                    saved.getUploadedAt()
            );

        } catch (IOException e) {
            return new DocumentResponse(false, "Failed to upload file: " + e.getMessage(), null, null, null, null);
        }
    }

    public DocumentResponse getDocument(Long documentId) {
        Optional<Document> docOpt = documentRepository.findById(documentId);
        if (docOpt.isEmpty()) {
            return new DocumentResponse(false, "Document not found", null, null, null, null);
        }

        Document doc = docOpt.get();
        return new DocumentResponse(
                true,
                "Document retrieved successfully",
                doc.getId(),
                doc.getFileName(),
                doc.getFileUrl(),
                doc.getUploadedAt()
        );
    }
}