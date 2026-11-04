package com.pharmames.service;

import com.pharmames.model.Batch;
import com.pharmames.model.BatchStage;
import com.pharmames.model.BatchStatus;
import com.pharmames.repository.BatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BatchService {

    private final BatchRepository batchRepository;
    private final AuditService auditService;

    public BatchService(BatchRepository batchRepository, AuditService auditService) {
        this.batchRepository = batchRepository;
        this.auditService = auditService;
    }

    public List<Batch> getAllBatches() {
        return batchRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Batch> getBatchById(Long id) {
        return batchRepository.findById(id);
    }

    public Optional<Batch> getBatchByNumber(String batchNumber) {
        return batchRepository.findByBatchNumber(batchNumber);
    }

    public List<Batch> filterByStatus(BatchStatus status) {
        if (status == null) {
            return getAllBatches();
        }
        return batchRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public List<Batch> searchBatches(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBatches();
        }
        return batchRepository.searchBatches(keyword.trim());
    }

    @Transactional
    public Batch createBatch(Batch batch, String operatorName, String reason) {
        if (operatorName == null || operatorName.trim().isEmpty()) {
            operatorName = batch.getOperatorInCharge() != null ? batch.getOperatorInCharge() : "Production Planner";
        }
        if (reason == null || reason.trim().isEmpty()) {
            reason = "Standard Production Order Initiation";
        }

        Batch saved = batchRepository.save(batch);

        auditService.logEvent(
                "BATCH",
                saved.getBatchNumber(),
                "CREATE_BATCH",
                operatorName,
                "Production Lead",
                reason,
                String.format("Created batch %s for %s (%s %s, Line: %s)",
                        saved.getBatchNumber(), saved.getProductName(), saved.getTargetQuantity(), saved.getUnit(), saved.getAssignedLine())
        );

        return saved;
    }

    @Transactional
    public Batch updateStatus(Long batchId, BatchStatus newStatus, String operatorName, String reason) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found with ID: " + batchId));

        BatchStatus oldStatus = batch.getStatus();
        batch.setStatus(newStatus);
        Batch updated = batchRepository.save(batch);

        auditService.logEvent(
                "BATCH",
                batch.getBatchNumber(),
                "STATUS_CHANGE",
                operatorName != null ? operatorName : "MES Supervisor",
                "Shift Supervisor",
                reason != null ? reason : "Operational status update",
                String.format("Changed batch %s status from %s to %s", batch.getBatchNumber(), oldStatus, newStatus)
        );

        return updated;
    }

    @Transactional
    public Batch advanceStage(Long batchId, String operatorName, String reason) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found with ID: " + batchId));

        BatchStage oldStage = batch.getCurrentStage();
        BatchStage nextStage = oldStage.getNextStage();

        batch.setCurrentStage(nextStage);
        if (batch.getStatus() == BatchStatus.PENDING) {
            batch.setStatus(BatchStatus.IN_PROGRESS);
        } else if (nextStage == BatchStage.RELEASED) {
            batch.setStatus(BatchStatus.COMPLETED);
        }

        Batch updated = batchRepository.save(batch);

        auditService.logEvent(
                "BATCH",
                batch.getBatchNumber(),
                "STAGE_ADVANCE",
                operatorName != null ? operatorName : "Line Supervisor",
                "Production Lead",
                reason != null ? reason : "Stage operations completed successfully",
                String.format("Advanced batch %s from stage [%s] to [%s]", batch.getBatchNumber(), oldStage.getDisplayName(), nextStage.getDisplayName())
        );

        return updated;
    }

    public long countTotal() {
        return batchRepository.count();
    }

    public long countByStatus(BatchStatus status) {
        return batchRepository.countByStatus(status);
    }
}
