package com.pharmames.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "quality_records")
public class QualityRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    @NotNull(message = "Assay percentage is required")
    @Column(nullable = false)
    private Double assayPercentage; // Target: 95.0% - 105.0%

    @NotNull(message = "Dissolution rate is required")
    @Column(nullable = false)
    private Double dissolutionRate; // Target: >= 80.0% in 45 min

    @NotNull(message = "Moisture content is required")
    @Column(nullable = false)
    private Double moistureContent; // Target: <= 3.0%

    @Column(nullable = false, length = 128)
    private String microbialStatus = "Conforms to USP/EP Specifications";

    @Column(nullable = false, length = 32)
    private String disposition = "UNDER_REVIEW"; // UNDER_REVIEW, RELEASED, QUARANTINED, REJECTED

    @Column(nullable = false, length = 64)
    private String inspectorName;

    @Column(length = 64)
    private String qaOfficer;

    @Column(length = 500)
    private String reviewNotes;

    @Column(nullable = false)
    private LocalDateTime reviewedAt;

    public QualityRecord() {
    }

    public QualityRecord(Batch batch, Double assayPercentage, Double dissolutionRate, Double moistureContent,
                         String microbialStatus, String disposition, String inspectorName, String qaOfficer,
                         String reviewNotes) {
        this.batch = batch;
        this.assayPercentage = assayPercentage;
        this.dissolutionRate = dissolutionRate;
        this.moistureContent = moistureContent;
        this.microbialStatus = microbialStatus != null ? microbialStatus : "Conforms to USP/EP Specifications";
        this.disposition = disposition != null ? disposition : "UNDER_REVIEW";
        this.inspectorName = inspectorName;
        this.qaOfficer = qaOfficer;
        this.reviewNotes = reviewNotes;
        this.reviewedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.reviewedAt == null) {
            this.reviewedAt = LocalDateTime.now();
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public Double getAssayPercentage() {
        return assayPercentage;
    }

    public void setAssayPercentage(Double assayPercentage) {
        this.assayPercentage = assayPercentage;
    }

    public Double getDissolutionRate() {
        return dissolutionRate;
    }

    public void setDissolutionRate(Double dissolutionRate) {
        this.dissolutionRate = dissolutionRate;
    }

    public Double getMoistureContent() {
        return moistureContent;
    }

    public void setMoistureContent(Double moistureContent) {
        this.moistureContent = moistureContent;
    }

    public String getMicrobialStatus() {
        return microbialStatus;
    }

    public void setMicrobialStatus(String microbialStatus) {
        this.microbialStatus = microbialStatus;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    public String getQaOfficer() {
        return qaOfficer;
    }

    public void setQaOfficer(String qaOfficer) {
        this.qaOfficer = qaOfficer;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}
