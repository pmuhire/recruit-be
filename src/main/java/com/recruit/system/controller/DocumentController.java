package com.recruit.system.controller;

import com.recruit.system.dto.response.DocumentResponse;
import com.recruit.system.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

//    @PostMapping("/upload/{applicationId}")
//    @PreAuthorize("hasAnyRole('APPLICANT','HR')")
//    public ResponseEntity<DocumentResponse> uploadDocument(
//            @PathVariable Long applicationId,
//            @RequestParam("file") MultipartFile file) {
//
//        DocumentResponse response = documentService.uploadDocument(applicationId, file);
//        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
//    }
//
//    @GetMapping("/{documentId}")
//    @PreAuthorize("hasAnyRole('APPLICANT','HR')")
//    public ResponseEntity<DocumentResponse> getDocument(@PathVariable Long documentId) {
//        DocumentResponse response = documentService.getDocument(documentId);
//        return ResponseEntity.status(response.isSuccess() ? 200 : 404).body(response);
//    }
}