package com.pharmames.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "batches")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Batch number is required")
    @Column(nullable = false, unique = true, length = 64)
    private String batchNumber;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false, length = 128)
    private String productName;

    @NotBlank(message = "Dosage form is required")
    @Column(nullable = false, length = 64)
    private String dosageForm; // e.g., Tablet, Capsule, Oral Suspension, Injectable

    @NotNull(message = "Target quantity is required")
    @Positive(message = "Target quantity must be greater than zero")
    @Column(nullable = false)
    private Double targetQuantity;

    @NotBlank(message = "Unit of measurement is required")
    @Column(nullable = false, length = 32)
    private String unit; // e.g., kg, Liters, Tablets, Vials

    @NotNull(message = "Manufacturing date is required")
    @Column(nullable = false)
    private LocalDate manufacturingDate;

    @NotNull(message = "Expiration date is required")
    @Column(nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private BatchStage currentStage = BatchStage.DISPENSING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private BatchStatus status = BatchStatus.PENDING;

    @Column(length = 64)
    private String assignedLine = "Production Line 1";

    @Column(length = 64)
    private String operatorInCharge = "Dr. M. Sharma";

    @Column(length = 500)
    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("stepNumber ASC")
    private List<ElectronicBatchRecord> ebrRecords = new ArrayList<>();

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<QualityRecord> qualityRecords = new ArrayList<>();

    public Batch() {
    }

    public Batch(String batchNumber, String productName, String dosageForm, Double targetQuantity, String unit,
                 LocalDate manufacturingDate, LocalDate expiryDate, BatchStage currentStage, BatchStatus status,
                 String assignedLine, String operatorInCharge, String notes) {
        this.batchNumber = batchNumber;
        this.productName = productName;
        this.dosageForm = dosageForm;
        this.targetQuantity = targetQuantity;
        this.unit = unit;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.currentStage = currentStage != null ? currentStage : BatchStage.DISPENSING;
        this.status = status != null ? status : BatchStatus.PENDING;
        this.assignedLine = assignedLine;
        this.operatorInCharge = operatorInCharge;
        this.notes = notes;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.currentStage == null) {
            this.currentStage = BatchStage.DISPENSING;
        }
        if (this.status == null) {
            this.status = BatchStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public Double getTargetQuantity() {
        return targetQuantity;
    }

    public void setTargetQuantity(Double targetQuantity) {
        this.targetQuantity = targetQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(LocalDate manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BatchStage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(BatchStage currentStage) {
        this.currentStage = currentStage;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public void setStatus(BatchStatus status) {
        this.status = status;
    }

    public String getAssignedLine() {
        return assignedLine;
    }

    public void setAssignedLine(String assignedLine) {
        this.assignedLine = assignedLine;
    }

    public String getOperatorInCharge() {
        return operatorInCharge;
    }

    public void setOperatorInCharge(String operatorInCharge) {
        this.operatorInCharge = operatorInCharge;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ElectronicBatchRecord> getEbrRecords() {
        return ebrRecords;
    }

    public void setEbrRecords(List<ElectronicBatchRecord> ebrRecords) {
        this.ebrRecords = ebrRecords;
    }

    public List<QualityRecord> getQualityRecords() {
        return qualityRecords;
    }

    public void setQualityRecords(List<QualityRecord> qualityRecords) {
        this.qualityRecords = qualityRecords;
    }
}
