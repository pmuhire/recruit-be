package com.recruit.system.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Service
public class SupabaseStorageService {

    private static final Logger log = LoggerFactory.getLogger(SupabaseStorageService.class);

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service.key}")
    private String serviceKey;

    @Value("${supabase.storage.bucket}")
    private String bucket;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String uploadFile(MultipartFile file, Long applicationId) {
        try {
            String originalFilename = file.getOriginalFilename() != null
                    ? file.getOriginalFilename().replace(" ", "_")
                    : "file";

            String filePath = "applications/" + applicationId + "/" + UUID.randomUUID() + "-" + originalFilename;
            String url = supabaseUrl + "/storage/v1/object/" + bucket + "/" + filePath;

            log.info("Uploading file to Supabase. bucket={}, applicationId={}, filePath={}", bucket, applicationId, filePath);
            log.debug("Supabase upload URL: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", serviceKey);
            headers.setBearerAuth(serviceKey);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set("x-upsert", "true");

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            log.info("Supabase upload response status={}", response.getStatusCode());
            log.debug("Supabase upload response body={}", response.getBody());

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException(
                        "Supabase upload failed. Status: " + response.getStatusCode()
                                + ", Body: " + response.getBody()
                );
            }

            return filePath;

        } catch (HttpStatusCodeException e) {
            log.error("Supabase upload HTTP error. status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException(
                    "Supabase upload failed. Status: " + e.getStatusCode()
                            + ", Body: " + e.getResponseBodyAsString(),
                    e
            );
        } catch (Exception e) {
            log.error("Supabase upload failed with unexpected error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file to Supabase Storage: " + e.getMessage(), e);
        }
    }

    public String createSignedUrl(String filePath) {
        try {
            String url = supabaseUrl + "/storage/v1/object/sign/" + bucket + "/" + filePath;

            log.info("Creating signed URL for filePath={} in bucket={}", filePath, bucket);
            log.debug("Supabase signed URL endpoint={}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", serviceKey);
            headers.setBearerAuth(serviceKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = """
                    {
                      "expiresIn": 3600
                    }
                    """;

            log.debug("Supabase signed URL request body={}", body);

            HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            log.info("Supabase signed URL response status={}", response.getStatusCode());
            log.debug("Supabase signed URL raw response body={}", response.getBody());

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException(
                        "Failed to generate signed URL. Status: " + response.getStatusCode()
                                + ", Body: " + response.getBody()
                );
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode signedUrlNode = root.get("signedURL");

            if (signedUrlNode == null || signedUrlNode.isNull() || signedUrlNode.asText().isBlank()) {
                log.error("signedURL field missing in Supabase response body={}", response.getBody());
                throw new RuntimeException("Signed URL not returned by Supabase. Body: " + response.getBody());
            }

            String finalUrl = supabaseUrl + "/storage/v1" + signedUrlNode.asText();

            log.info("Signed URL created successfully for filePath={}", filePath);
            log.debug("Final signed URL={}", finalUrl);

            return finalUrl;

        } catch (HttpStatusCodeException e) {
            log.error("Supabase signed URL HTTP error. status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException(
                    "Create signed URL failed. Status: " + e.getStatusCode()
                            + ", Body: " + e.getResponseBodyAsString(),
                    e
            );
        } catch (Exception e) {
            log.error("Supabase signed URL generation failed for filePath={}: {}", filePath, e.getMessage(), e);
            throw new RuntimeException("Failed to create signed URL: " + e.getMessage(), e);
        }
    }
}