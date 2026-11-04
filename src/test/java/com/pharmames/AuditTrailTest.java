package com.pharmames;

import com.pharmames.model.AuditTrail;
import com.pharmames.service.AuditService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuditTrailTest {

    @Autowired
    private AuditService auditService;

    @Test
    void testAuditLogCreationAndRetrieval() {
        AuditTrail log = auditService.logEvent(
                "BATCH",
                "TEST-AUDIT-001",
                "STAGE_ADVANCE",
                "Dr. John Doe",
                "Qualified Person",
                "Routine in-process control check",
                "Completed Dispensing stage operations within tolerances"
        );

        assertNotNull(log.getId());
        assertNotNull(log.getTimestamp());
        assertEquals("TEST-AUDIT-001", log.getEntityIdentifier());
        assertEquals("STAGE_ADVANCE", log.getAction());
        assertEquals("Dr. John Doe", log.getPerformedBy());

        List<AuditTrail> batchLogs = auditService.getLogsForBatch("TEST-AUDIT-001");
        assertFalse(batchLogs.isEmpty());
        assertEquals("STAGE_ADVANCE", batchLogs.get(0).getAction());
    }

    @Test
    void testSearchAuditLogs() {
        auditService.logEvent(
                "EBR_RECORD",
                "TEST-SEARCH-002",
                "EBR_STEP_RECORDED",
                "Alice Smith",
                "Operator",
                "Testing search query",
                "UniqueKeywordExcursionRecorded"
        );

        List<AuditTrail> searchResults = auditService.searchLogs("UniqueKeywordExcursionRecorded");
        assertFalse(searchResults.isEmpty());
        assertEquals("Alice Smith", searchResults.get(0).getPerformedBy());
    }
}
