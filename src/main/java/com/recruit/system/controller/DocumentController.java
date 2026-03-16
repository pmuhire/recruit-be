package com.recruit.system.controller;

import com.recruit.system.model.Application;
import com.recruit.system.model.Document;
import com.recruit.system.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload/{applicationId}")
    public ResponseEntity<Document> upload(@PathVariable Long applicationId,
                                           @RequestParam("file") MultipartFile file) throws IOException {
        Application app = new Application(); // Fetch application by ID from repo
        app.setId(applicationId);

        Document doc = documentService.uploadDocument(file, app);
        return ResponseEntity.ok(doc);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<String> download(@PathVariable Long id) {
        String url = documentService.downloadDocument(id);
        return ResponseEntity.ok(url); // Returns the Cloudinary URL
    }
}