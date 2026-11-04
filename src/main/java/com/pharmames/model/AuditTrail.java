package com.pharmames.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_trails")
public class AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, length = 64)
    private String entityType; // BATCH, EBR_RECORD, QUALITY_RECORD, SYSTEM

    @Column(nullable = false, length = 64)
    private String entityIdentifier; // Batch Number or ID

    @Column(nullable = false, length = 64)
    private String action; // CREATE, STAGE_ADVANCE, STATUS_CHANGE, EBR_SIGNOFF, QC_EVALUATION

    @Column(nullable = false, length = 64)
    private String performedBy;

    @Column(length = 64)
    private String userRole;

    @Column(length = 255)
    private String reasonForChange;

    @Column(length = 1000)
    private String details;

    public AuditTrail() {
    }

    public AuditTrail(String entityType, String entityIdentifier, String action, String performedBy,
                      String userRole, String reasonForChange, String details) {
        this.timestamp = LocalDateTime.now();
        this.entityType = entityType;
        this.entityIdentifier = entityIdentifier;
        this.action = action;
        this.performedBy = performedBy;
        this.userRole = userRole != null ? userRole : "System Operator";
        this.reasonForChange = reasonForChange;
        this.details = details;
    }

    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityIdentifier() {
        return entityIdentifier;
    }

    public void setEntityIdentifier(String entityIdentifier) {
        this.entityIdentifier = entityIdentifier;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getReasonForChange() {
        return reasonForChange;
    }

    public void setReasonForChange(String reasonForChange) {
        this.reasonForChange = reasonForChange;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
