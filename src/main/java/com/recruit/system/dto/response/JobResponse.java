package com.recruit.system.dto.response;

import com.recruit.system.model.Job;
import java.time.LocalDateTime;

public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String requirements;
    private Job.Status status;
    private LocalDateTime createdAt;

    public JobResponse() {}

    public JobResponse(Long id, String title, String description,
                       String requirements, Job.Status status,
                       LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirements() {
        return requirements;
    }

    public Job.Status getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public void setStatus(Job.Status status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}