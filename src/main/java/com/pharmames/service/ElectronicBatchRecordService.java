package com.pharmames.service;

import com.pharmames.model.Batch;
import com.pharmames.model.ElectronicBatchRecord;
import com.pharmames.repository.BatchRepository;
import com.pharmames.repository.ElectronicBatchRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ElectronicBatchRecordService {

    private final ElectronicBatchRecordRepository ebrRepository;
    private final BatchRepository batchRepository;
    private final AuditService auditService;

    public ElectronicBatchRecordService(ElectronicBatchRecordRepository ebrRepository,
                                       BatchRepository batchRepository,
                                       AuditService auditService) {
        this.ebrRepository = ebrRepository;
        this.batchRepository = batchRepository;
        this.auditService = auditService;
    }

    public List<ElectronicBatchRecord> getAllRecords() {
        return ebrRepository.findAllByOrderBySignedAtDesc();
    }

    public List<ElectronicBatchRecord> getRecentRecords() {
        return ebrRepository.findTop10ByOrderBySignedAtDesc();
    }

    public List<ElectronicBatchRecord> getRecordsForBatch(Long batchId) {
        return ebrRepository.findByBatchIdOrderByStepNumberAsc(batchId);
    }

    @Transactional
    public ElectronicBatchRecord recordStep(Long batchId, ElectronicBatchRecord record) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found with ID: " + batchId));

        record.setBatch(batch);
        if (record.getStage() == null) {
            record.setStage(batch.getCurrentStage());
        }

        ElectronicBatchRecord saved = ebrRepository.save(record);

        auditService.logEvent(
                "EBR_RECORD",
                batch.getBatchNumber(),
                "EBR_STEP_RECORDED",
                record.getOperatorName(),
                "Qualified Operator",
                "Manufacturing process execution step sign-off",
                String.format("Step #%d: '%s' recorded for stage [%s]. Result: %s. Measured: %s",
                        saved.getStepNumber(), saved.getStepName(), saved.getStage().getDisplayName(), saved.getStatus(), saved.getActualParameters())
        );

        return saved;
    }
}
