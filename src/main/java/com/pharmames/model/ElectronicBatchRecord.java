package com.pharmames.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "electronic_batch_records")
public class ElectronicBatchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    @NotNull(message = "Step number is required")
    @Column(nullable = false)
    private Integer stepNumber;

    @NotBlank(message = "Step name is required")
    @Column(nullable = false, length = 128)
    private String stepName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private BatchStage stage;

    @Column(length = 255)
    private String targetParameters; // e.g. "Temp: 20-25°C, Impeller: 120-150 RPM"

    @NotBlank(message = "Actual measured parameters are required")
    @Column(nullable = false, length = 255)
    private String actualParameters; // e.g. "Temp: 22.8°C, Impeller: 140 RPM"

    @NotBlank(message = "Operator signature/name is required")
    @Column(nullable = false, length = 64)
    private String operatorName;

    @Column(length = 64)
    private String supervisorName;

    @NotBlank(message = "Step status is required")
    @Column(nullable = false, length = 32)
    private String status = "PASS"; // PASS, FAIL, DEVIATION

    @Column(length = 500)
    private String deviationDetails;

    @Column(length = 500)
    private String comments;

    @Column(nullable = false)
    private LocalDateTime signedAt;

    public ElectronicBatchRecord() {
    }

    public ElectronicBatchRecord(Batch batch, Integer stepNumber, String stepName, BatchStage stage,
                                 String targetParameters, String actualParameters, String operatorName,
                                 String supervisorName, String status, String deviationDetails, String comments) {
        this.batch = batch;
        this.stepNumber = stepNumber;
        this.stepName = stepName;
        this.stage = stage;
        this.targetParameters = targetParameters;
        this.actualParameters = actualParameters;
        this.operatorName = operatorName;
        this.supervisorName = supervisorName;
        this.status = status != null ? status : "PASS";
        this.deviationDetails = deviationDetails;
        this.comments = comments;
        this.signedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.signedAt == null) {
            this.signedAt = LocalDateTime.now();
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

    public Integer getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public BatchStage getStage() {
        return stage;
    }

    public void setStage(BatchStage stage) {
        this.stage = stage;
    }

    public String getTargetParameters() {
        return targetParameters;
    }

    public void setTargetParameters(String targetParameters) {
        this.targetParameters = targetParameters;
    }

    public String getActualParameters() {
        return actualParameters;
    }

    public void setActualParameters(String actualParameters) {
        this.actualParameters = actualParameters;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviationDetails() {
        return deviationDetails;
    }

    public void setDeviationDetails(String deviationDetails) {
        this.deviationDetails = deviationDetails;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getSignedAt() {
        return signedAt;
    }

    public void setSignedAt(LocalDateTime signedAt) {
        this.signedAt = signedAt;
    }
}
