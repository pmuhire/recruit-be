package com.recruit.system.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentResponse {

    private Long id;

    private String fileName;

    private String fileUrl;

    private LocalDateTime uploadedAt;

}