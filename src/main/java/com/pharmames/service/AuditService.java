package com.pharmames.service;

import com.pharmames.model.AuditTrail;
import com.pharmames.repository.AuditTrailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditService {

    private final AuditTrailRepository auditTrailRepository;

    public AuditService(AuditTrailRepository auditTrailRepository) {
        this.auditTrailRepository = auditTrailRepository;
    }

    @Transactional
    public AuditTrail logEvent(String entityType, String entityIdentifier, String action,
                               String performedBy, String userRole, String reasonForChange, String details) {
        AuditTrail record = new AuditTrail(entityType, entityIdentifier, action, performedBy, userRole, reasonForChange, details);
        return auditTrailRepository.save(record);
    }

    public List<AuditTrail> getAllLogs() {
        return auditTrailRepository.findAllByOrderByTimestampDesc();
    }

    public List<AuditTrail> getRecentLogs() {
        return auditTrailRepository.findTop15ByOrderByTimestampDesc();
    }

    public List<AuditTrail> getLogsForBatch(String batchNumber) {
        return auditTrailRepository.findByEntityIdentifierOrderByTimestampDesc(batchNumber);
    }

    public List<AuditTrail> searchLogs(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllLogs();
        }
        return auditTrailRepository.searchAuditLogs(query.trim());
    }
}
