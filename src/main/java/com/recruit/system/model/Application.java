package com.recruit.system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

    private String status;

    private String reviewReason;

    private LocalDateTime submittedAt;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private Users reviewedBy;

    private LocalDateTime reviewedAt;

    // Default constructor
    public Application() {
    }

    // Full constructor
    public Application(Long id, Applicant applicant, String status, String reviewReason,
                       LocalDateTime submittedAt, Users reviewedBy, LocalDateTime reviewedAt) {
        this.id = id;
        this.applicant = applicant;
        this.status = status;
        this.reviewReason = reviewReason;
        this.submittedAt = submittedAt;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = reviewedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReviewReason() {
        return reviewReason;
    }

    public void setReviewReason(String reviewReason) {
        this.reviewReason = reviewReason;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Users getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Users reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}