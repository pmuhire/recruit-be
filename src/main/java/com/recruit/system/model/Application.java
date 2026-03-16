package com.recruit.system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "applications",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "job_id"})
        }
)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private String reviewReason;

    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    public Application() {}

    // ---------- Getters ----------
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Job getJob() {
        return job;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public String getReviewReason() {
        return reviewReason;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    // ---------- Setters ----------
    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public void setReviewReason(String reviewReason) {
        this.reviewReason = reviewReason;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    // ---------- Helper Methods ----------
    public void addDocument(Document document) {
        documents.add(document);
        document.setApplication(this);
    }

    public void removeDocument(Document document) {
        documents.remove(document);
        document.setApplication(null);
    }
}