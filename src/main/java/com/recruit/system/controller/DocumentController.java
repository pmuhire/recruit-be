package com.recruit.system.controller;

import com.recruit.system.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/{documentId}/view-url")
    public ResponseEntity<Map<String, String>> getDocumentViewUrl(@PathVariable Long documentId) {
        log.info("GET /api/documents/{}/view-url called", documentId);

        String viewUrl = documentService.generateDocumentViewUrlById(documentId);

        log.info("Returning signed URL response for documentId={}", documentId);
        return ResponseEntity.ok(Map.of("url", viewUrl));
    }
}