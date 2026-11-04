package com.pharmames.service;

import com.pharmames.model.Batch;
import com.pharmames.model.BatchStage;
import com.pharmames.model.BatchStatus;
import com.pharmames.model.QualityRecord;
import com.pharmames.repository.BatchRepository;
import com.pharmames.repository.QualityRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QualityService {

    private final QualityRecordRepository qualityRecordRepository;
    private final BatchRepository batchRepository;
    private final AuditService auditService;

    public QualityService(QualityRecordRepository qualityRecordRepository,
                          BatchRepository batchRepository,
                          AuditService auditService) {
        this.qualityRecordRepository = qualityRecordRepository;
        this.batchRepository = batchRepository;
        this.auditService = auditService;
    }

    public List<QualityRecord> getAllRecords() {
        return qualityRecordRepository.findAllByOrderByReviewedAtDesc();
    }

    public List<QualityRecord> getRecordsForBatch(Long batchId) {
        return qualityRecordRepository.findByBatchIdOrderByReviewedAtDesc(batchId);
    }

    @Transactional
    public QualityRecord submitReview(Long batchId, QualityRecord record) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found with ID: " + batchId));

        record.setBatch(batch);
        QualityRecord saved = qualityRecordRepository.save(record);

        // Update batch state based on QA disposition
        String disposition = record.getDisposition();
        if ("APPROVED".equalsIgnoreCase(disposition)) {
            batch.setStatus(BatchStatus.COMPLETED);
            batch.setCurrentStage(BatchStage.RELEASED);
        } else if ("QUARANTINED".equalsIgnoreCase(disposition)) {
            batch.setStatus(BatchStatus.ON_HOLD);
        } else if ("REJECTED".equalsIgnoreCase(disposition)) {
            batch.setStatus(BatchStatus.REJECTED);
        } else {
            batch.setCurrentStage(BatchStage.QUALITY_CONTROL);
        }
        batchRepository.save(batch);

        auditService.logEvent(
                "QUALITY_RECORD",
                batch.getBatchNumber(),
                "QA_DISPOSITION_" + disposition.toUpperCase(),
                record.getQaOfficer() != null ? record.getQaOfficer() : record.getInspectorName(),
                "Quality Assurance Officer",
                "Formal Quality Disposition Sign-Off",
                String.format("Batch %s evaluated with disposition [%s]. Assay: %.2f%%, Dissolution: %.2f%%, Moisture: %.2f%%. Notes: %s",
                        batch.getBatchNumber(), disposition, record.getAssayPercentage(), record.getDissolutionRate(),
                        record.getMoistureContent(), record.getReviewNotes() != null ? record.getReviewNotes() : "N/A")
        );

        return saved;
    }

    public long countByDisposition(String disposition) {
        return qualityRecordRepository.countByDisposition(disposition);
    }
}
