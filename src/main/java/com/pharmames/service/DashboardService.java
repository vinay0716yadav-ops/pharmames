package com.pharmames.service;

import com.pharmames.model.Batch;
import com.pharmames.model.BatchStage;
import com.pharmames.model.BatchStatus;
import com.pharmames.repository.BatchRepository;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final BatchRepository batchRepository;
    private final AuditService auditService;
    private final ElectronicBatchRecordService ebrService;

    public DashboardService(BatchRepository batchRepository,
                            AuditService auditService,
                            ElectronicBatchRecordService ebrService) {
        this.batchRepository = batchRepository;
        this.auditService = auditService;
        this.ebrService = ebrService;
    }

    public long getTotalBatches() {
        return batchRepository.count();
    }

    public long getActiveBatches() {
        return batchRepository.countByStatus(BatchStatus.IN_PROGRESS);
    }

    public long getCompletedBatches() {
        return batchRepository.countByStatus(BatchStatus.COMPLETED);
    }

    public long getPendingBatches() {
        return batchRepository.countByStatus(BatchStatus.PENDING);
    }

    public long getOnHoldBatches() {
        return batchRepository.countByStatus(BatchStatus.ON_HOLD);
    }

    public long getRejectedBatches() {
        return batchRepository.countByStatus(BatchStatus.REJECTED);
    }

    public Map<BatchStage, Long> getStageDistribution() {
        Map<BatchStage, Long> map = new EnumMap<>(BatchStage.class);
        for (BatchStage stage : BatchStage.values()) {
            map.put(stage, (long) batchRepository.findByCurrentStageOrderByCreatedAtDesc(stage).size());
        }
        return map;
    }

    public List<Batch> getRecentBatches() {
        List<Batch> batches = batchRepository.findAllByOrderByCreatedAtDesc();
        return batches.size() > 5 ? batches.subList(0, 5) : batches;
    }
}
