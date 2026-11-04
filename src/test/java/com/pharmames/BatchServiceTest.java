package com.pharmames;

import com.pharmames.model.Batch;
import com.pharmames.model.BatchStage;
import com.pharmames.model.BatchStatus;
import com.pharmames.repository.BatchRepository;
import com.pharmames.service.BatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BatchServiceTest {

    @Autowired
    private BatchService batchService;

    @Autowired
    private BatchRepository batchRepository;

    @Test
    void testCreateBatch() {
        Batch batch = new Batch(
                "TEST-BAT-999",
                "Aspirin 100mg Tablets",
                "Tablet",
                100000.0,
                "Tablets",
                LocalDate.now(),
                LocalDate.now().plusYears(2),
                BatchStage.DISPENSING,
                BatchStatus.PENDING,
                "Line 1 - Solid Dose Rotary",
                "Test Chemist",
                "Automated test batch"
        );

        Batch created = batchService.createBatch(batch, "QA Tester", "Unit testing batch creation");

        assertNotNull(created.getId());
        assertEquals("TEST-BAT-999", created.getBatchNumber());
        assertEquals(BatchStatus.PENDING, created.getStatus());
        assertEquals(BatchStage.DISPENSING, created.getCurrentStage());
    }

    @Test
    void testAdvanceBatchStage() {
        Batch batch = new Batch(
                "TEST-BAT-ADV",
                "Ciprofloxacin 500mg",
                "Tablet",
                50000.0,
                "Tablets",
                LocalDate.now(),
                LocalDate.now().plusYears(2),
                BatchStage.DISPENSING,
                BatchStatus.PENDING,
                "Line 1 - Solid Dose Rotary",
                "Test Chemist",
                "Test advance"
        );
        Batch created = batchService.createBatch(batch, "Tester", "Init");

        Batch advanced = batchService.advanceStage(created.getId(), "Line Lead", "Advancing to next stage");
        assertEquals(BatchStage.GRANULATION, advanced.getCurrentStage());
        assertEquals(BatchStatus.IN_PROGRESS, advanced.getStatus());
    }

    @Test
    void testUpdateBatchStatusToHold() {
        Batch batch = new Batch(
                "TEST-BAT-HOLD",
                "Amoxicillin 500mg",
                "Capsule",
                20000.0,
                "Capsules",
                LocalDate.now(),
                LocalDate.now().plusYears(2),
                BatchStage.DISPENSING,
                BatchStatus.IN_PROGRESS,
                "Line 1",
                "Test Lead",
                "Hold test"
        );
        Batch created = batchService.createBatch(batch, "Tester", "Init");

        Batch held = batchService.updateStatus(created.getId(), BatchStatus.ON_HOLD, "QA Lead", "Deviation review");
        assertEquals(BatchStatus.ON_HOLD, held.getStatus());
    }
}
