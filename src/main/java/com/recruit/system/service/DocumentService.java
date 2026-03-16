package com.recruit.system.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.recruit.system.model.Application;
import com.recruit.system.model.Document;
import com.recruit.system.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class DocumentService {

    private final Cloudinary cloudinary;
    private final DocumentRepository documentRepository;

    public DocumentService(Cloudinary cloudinary, DocumentRepository documentRepository) {
        this.cloudinary = cloudinary;
        this.documentRepository = documentRepository;
    }

    public Document uploadDocument(MultipartFile file, Application application) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto")); // auto for PDF/DOCX

        String url = uploadResult.get("secure_url").toString();
        String fileName = file.getOriginalFilename();

        Document doc = new Document();
        doc.setApplication(application);
        doc.setFileName(fileName);
        doc.setFileUrl(url);
        doc.setUploadedAt(LocalDateTime.now());

        return documentRepository.save(doc);
    }

    public String downloadDocument(Long documentId) {
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
        return doc.getFileUrl(); // Return Cloudinary URL
    }
}