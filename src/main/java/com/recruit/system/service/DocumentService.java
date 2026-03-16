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
    public DocumentResponse uploadDocumentForApplication(Application application, MultipartFile file) {
        try {
            // Upload file to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "applications/" + application.getId()
            ));

            String fileUrl = (String) uploadResult.get("secure_url");

            // Create document and associate with the application
            Document document = new Document();
            document.setFileName(file.getOriginalFilename());
            document.setFileUrl(fileUrl);
            document.setUploadedAt(LocalDateTime.now());

            // Add document to the application (this handles the bidirectional relationship)
            application.addDocument(document);

            // Save document before saving the application (or rely on cascade)
            documentRepository.save(document); // Save document separately if needed

            return new DocumentResponse(true, "CV uploaded successfully",
                    document.getId(), document.getFileName(), document.getFileUrl(), document.getUploadedAt());

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload CV: " + e.getMessage());
        }
    }
    /**
     * Upload a document (CV) and associate it with an application.
     * Ensures that the document is linked to the application's documents list.
     */
    public DocumentResponse uploadDocument(Long applicationId, MultipartFile file) {
        Optional<Application> appOpt = applicationRepository.findById(applicationId);
        if (appOpt.isEmpty()) {
            return new DocumentResponse(false, "Application not found", null, null, null, null);
        }

        Application application = appOpt.get();

        try {
            // Upload file to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "applications/" + applicationId
            ));

            String fileUrl = (String) uploadResult.get("secure_url");

            // Create document and set bidirectional relationship
            Document document = new Document();
            document.setFileName(file.getOriginalFilename());
            document.setFileUrl(fileUrl);
            document.setUploadedAt(LocalDateTime.now());
            application.addDocument(document); // automatically sets document.application

            // Save the application, cascade will save the document
            applicationRepository.save(application);

            return new DocumentResponse(
                    true,
                    "File uploaded successfully",
                    document.getId(),
                    document.getFileName(),
                    document.getFileUrl(),
                    document.getUploadedAt()
            );

        } catch (IOException e) {
            return new DocumentResponse(false, "Failed to upload file: " + e.getMessage(), null, null, null, null);
        }
    }

    /**
     * Retrieve a document by its ID
     */
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

    /**
     * Optional: Remove a document from an application
     */
    public DocumentResponse deleteDocument(Long documentId) {
        Optional<Document> docOpt = documentRepository.findById(documentId);
        if (docOpt.isEmpty()) {
            return new DocumentResponse(false, "Document not found", null, null, null, null);
        }

        Document document = docOpt.get();
        Application application = document.getApplication();

        if (application != null) {
            application.removeDocument(document); // removes from list and nulls relationship
            applicationRepository.save(application); // cascades removal
        }

        return new DocumentResponse(
                true,
                "Document deleted successfully",
                document.getId(),
                document.getFileName(),
                document.getFileUrl(),
                document.getUploadedAt()
        );
    }
}