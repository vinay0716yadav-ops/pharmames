package com.pharmames.config;

import com.pharmames.model.*;
import com.pharmames.repository.*;
import com.pharmames.service.AuditService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BatchRepository batchRepository;
    private final ElectronicBatchRecordRepository ebrRepository;
    private final QualityRecordRepository qualityRepository;
    private final AuditService auditService;

    public DataInitializer(BatchRepository batchRepository,
                           ElectronicBatchRecordRepository ebrRepository,
                           QualityRecordRepository qualityRepository,
                           AuditService auditService) {
        this.batchRepository = batchRepository;
        this.ebrRepository = ebrRepository;
        this.qualityRepository = qualityRepository;
        this.auditService = auditService;
    }

    @Override
    public void run(String... args) {
        // Permanent storage check: If permanent database file already contains batches, do not duplicate!
        if (batchRepository.count() > 0) {
            return;
        }

        // ==========================================
        // 1. Batch BAT-2026-001 (Completed & Released)
        // ==========================================
        Batch b1 = new Batch(
                "BAT-2026-001",
                "Paracetamol 500mg Tablets",
                "Tablet",
                500000.0,
                "Tablets",
                LocalDate.now().minusDays(12),
                LocalDate.now().plusYears(3),
                BatchStage.RELEASED,
                BatchStatus.COMPLETED,
                "Line 1 - Solid Dose Rotary",
                "Vinay Yadav (Formulation Lead)",
                "Commercial batch manufactured at Vinay Pharma Unit 1. All critical quality attributes and control points fully compliant."
        );
        b1 = batchRepository.save(b1);
        auditService.logEvent("BATCH", b1.getBatchNumber(), "CREATE_BATCH", "Vinay Yadav", "Head of Production", "Commercial Campaign Order", "Initiated manufacturing batch BAT-2026-001");

        ebrRepository.save(new ElectronicBatchRecord(b1, 1, "Raw Material Dispensing & Verification", BatchStage.DISPENSING,
                "API Purity: >=99.5%, Excipient Loss on Drying <0.5%", "API Purity: 99.8%, LOD: 0.32%", "Vinay Yadav", "Dr. A. Verma", "PASS", null, "Double-check scale calibration verified."));
        ebrRepository.save(new ElectronicBatchRecord(b1, 2, "High-Shear Wet Granulation", BatchStage.GRANULATION,
                "Impeller: 140-160 RPM, Chopper: 1200 RPM, Mass Temp: 24-28°C", "Impeller: 150 RPM, Chopper: 1200 RPM, Temp: 26.2°C", "Vinay Yadav", "Dr. A. Verma", "PASS", null, "Granule endpoint reached in 8.5 min."));
        ebrRepository.save(new ElectronicBatchRecord(b1, 3, "Rotary Tablet Compression", BatchStage.COMPRESSION,
                "Main Compression Force: 15-20 kN, Hardness: 60-80 N", "Main Force: 17.4 kN, Hardness: 72 N avg", "J. Smith", "Vinay Yadav", "PASS", null, "Thickness and weight uniformity within specification."));
        ebrRepository.save(new ElectronicBatchRecord(b1, 4, "Aqueous Film Coating", BatchStage.COATING,
                "Inlet Air Temp: 60-65°C, Bed Temp: 42-46°C, Spray Rate: 120 g/min", "Inlet Temp: 62.5°C, Bed Temp: 44.1°C, Spray: 118 g/min", "J. Smith", "Vinay Yadav", "PASS", null, "Uniform film finish without orange peel effect."));
        ebrRepository.save(new ElectronicBatchRecord(b1, 5, "Blister Packaging & Track-and-Trace Serialization", BatchStage.PACKAGING,
                "Sealing Temp: 145-155°C, 2D Matrix Camera Verification: 100%", "Sealing Temp: 149°C, Camera Read: 100% Verified", "T. Miller", "Vinay Yadav", "PASS", null, "All carton serialization codes matched central repository."));

        QualityRecord q1 = new QualityRecord(b1, 99.8, 96.5, 1.4, "Conforms to USP/EP Specifications (Sterile/Non-growth)", "APPROVED", "R. Henderson (QC)", "Vinay Yadav (QA Director)", "All testing passed with high confidence. Approved for market release.");
        qualityRepository.save(q1);
        auditService.logEvent("QUALITY_RECORD", b1.getBatchNumber(), "QA_DISPOSITION_APPROVED", "Vinay Yadav", "QA Director / QP", "Final Batch Certification", "Batch BAT-2026-001 certified and released for distribution.");

        // ==========================================
        // 2. Batch BAT-2026-002 (In Progress - Granulation)
        // ==========================================
        Batch b2 = new Batch(
                "BAT-2026-002",
                "Amoxicillin 250mg Oral Suspension",
                "Oral Suspension",
                10000.0,
                "Liters",
                LocalDate.now().minusDays(3),
                LocalDate.now().plusYears(2),
                BatchStage.GRANULATION,
                BatchStatus.IN_PROGRESS,
                "Line 2 - Liquid Sterile Suite",
                "Vinay Yadav (Formulation Lead)",
                "Antibiotic formulation suite with dedicated HVAC positive pressure."
        );
        b2 = batchRepository.save(b2);
        auditService.logEvent("BATCH", b2.getBatchNumber(), "CREATE_BATCH", "Vinay Yadav", "Line Supervisor", "Production Schedule", "Created batch for Amoxicillin Oral Suspension");

        ebrRepository.save(new ElectronicBatchRecord(b2, 1, "Sterile Weighing & Active Dispensing", BatchStage.DISPENSING,
                "Ambient Temp: 18-22°C, RH: <40%", "Ambient Temp: 19.5°C, RH: 36%", "S. Jenkins", "Vinay Yadav", "PASS", null, "HEPA filtration laminar airflow velocity 0.45 m/s verified."));
        ebrRepository.save(new ElectronicBatchRecord(b2, 2, "Fluid Bed Granulation & Drying", BatchStage.GRANULATION,
                "Air Volume: 450 m3/h, Product Temp: 40-45°C", "Air Volume: 452 m3/h, Product Temp: 42.1°C", "S. Jenkins", "Vinay Yadav", "PASS", null, "Moisture check sample pulled for in-process lab testing."));

        // ==========================================
        // 3. Batch BAT-2026-003 (Quality Control Review)
        // ==========================================
        Batch b3 = new Batch(
                "BAT-2026-003",
                "Metformin HCl 850mg ER Tablets",
                "Tablet",
                250000.0,
                "Tablets",
                LocalDate.now().minusDays(7),
                LocalDate.now().plusYears(3),
                BatchStage.QUALITY_CONTROL,
                BatchStatus.IN_PROGRESS,
                "Line 1 - Solid Dose Rotary",
                "Vinay Yadav (Formulation Lead)",
                "Extended release matrix formulation. Batch in analytical laboratory testing."
        );
        b3 = batchRepository.save(b3);
        auditService.logEvent("BATCH", b3.getBatchNumber(), "CREATE_BATCH", "Vinay Yadav", "Production Lead", "Scheduled Order", "Order initiated for Metformin ER");

        ebrRepository.save(new ElectronicBatchRecord(b3, 1, "Excipient Polymer Weighing", BatchStage.DISPENSING,
                "HPMC Matrix Polymer Weight: 42.5 kg +/- 0.1", "Actual Weight: 42.52 kg", "D. Chen", "Vinay Yadav", "PASS", null, "Weight calibration checked."));
        ebrRepository.save(new ElectronicBatchRecord(b3, 2, "Dry Blending & Lubrication", BatchStage.GRANULATION,
                "V-Blender Speed: 18 RPM, Duration: 20 min", "Speed: 18 RPM, Duration: 20 min", "D. Chen", "Vinay Yadav", "PASS", null, "Uniformity blend sampling completed."));
        ebrRepository.save(new ElectronicBatchRecord(b3, 3, "High-Tonnage Compression", BatchStage.COMPRESSION,
                "Average Tablet Weight: 1050 mg +/- 2%", "Average Weight: 1048 mg", "D. Chen", "Vinay Yadav", "PASS", null, "Friability test: 0.18% (<1.0%)."));

        QualityRecord q3 = new QualityRecord(b3, 98.6, 91.2, 2.1, "Under Incubation Testing (48h)", "UNDER_REVIEW", "R. Henderson (QC)", "Vinay Yadav", "Assay and 12-hour dissolution profiles conforming; awaiting final microbial growth readout.");
        qualityRepository.save(q3);
        auditService.logEvent("QUALITY_RECORD", b3.getBatchNumber(), "QA_DISPOSITION_UNDER_REVIEW", "Vinay Yadav", "QC Director", "Routine QC submission", "Samples logged into QC analytical laboratory.");

        // ==========================================
        // 4. Batch BAT-2026-004 (Pending Initiation)
        // ==========================================
        Batch b4 = new Batch(
                "BAT-2026-004",
                "Insulin Glargine 100 U/mL",
                "Injectable",
                25000.0,
                "Vials",
                LocalDate.now(),
                LocalDate.now().plusYears(2),
                BatchStage.DISPENSING,
                BatchStatus.PENDING,
                "Line 3 - Biologics Aseptic",
                "Vinay Yadav (Bioprocess Engineer)",
                "Awaiting sterile media fill challenge validation before dispensing authorization."
        );
        batchRepository.save(b4);
        auditService.logEvent("BATCH", b4.getBatchNumber(), "CREATE_BATCH", "Vinay Yadav", "Bioprocess Engineer", "Production Batch Order", "Created batch order BAT-2026-004 for Insulin Glargine.");

        // ==========================================
        // 5. Batch BAT-2026-005 (On Hold / Deviation)
        // ==========================================
        Batch b5 = new Batch(
                "BAT-2026-005",
                "Atorvastatin 20mg Tablets",
                "Tablet",
                300000.0,
                "Tablets",
                LocalDate.now().minusDays(4),
                LocalDate.now().plusYears(3),
                BatchStage.COMPRESSION,
                BatchStatus.ON_HOLD,
                "Line 4 - High Speed Rotary",
                "Vinay Yadav (Formulation Lead)",
                "Quarantined on hold due to compression station punch binding excursion. QA investigation active."
        );
        b5 = batchRepository.save(b5);
        auditService.logEvent("BATCH", b5.getBatchNumber(), "CREATE_BATCH", "Vinay Yadav", "Line Supervisor", "Production Schedule", "Initiated batch BAT-2026-005");

        ebrRepository.save(new ElectronicBatchRecord(b5, 1, "Weighing & Sifting", BatchStage.DISPENSING,
                "Sieve Size: #30 mesh, Speed: 60 RPM", "Conforms to #30 mesh", "M. Brody", "Vinay Yadav", "PASS", null, "Sieve integrity verified before and after."));
        ebrRepository.save(new ElectronicBatchRecord(b5, 2, "Roller Compaction & Milling", BatchStage.GRANULATION,
                "Hydraulic Roll Pressure: 4.5 MPa", "Roll Pressure: 4.5 MPa", "M. Brody", "Vinay Yadav", "PASS", null, "Flake density within standard specifications."));
        ebrRepository.save(new ElectronicBatchRecord(b5, 3, "Rotary Compression (Upper Punch 12)", BatchStage.COMPRESSION,
                "Punch Temperature: <35°C", "Punch Temp spiked to 41.2°C at punch #12", "M. Brody", "Vinay Yadav", "DEVIATION", "Deviation DEV-2026-089: Punch overheating noted during 100k tablet checkpoint.", "Station halted immediately and placed in Quarantine."));

        auditService.logEvent("BATCH", b5.getBatchNumber(), "STATUS_CHANGE", "Vinay Yadav", "QA Lead", "Deviation DEV-2026-089", "Batch placed ON_HOLD pending metallurgical and dissolution investigation.");

        // ==========================================
        // 6. Batch BAT-2026-006 (In Progress - Coating)
        // ==========================================
        Batch b6 = new Batch(
                "BAT-2026-006",
                "Azithromycin 500mg Film-Coated",
                "Tablet",
                150000.0,
                "Tablets",
                LocalDate.now().minusDays(2),
                LocalDate.now().plusYears(3),
                BatchStage.COATING,
                BatchStatus.IN_PROGRESS,
                "Line 1 - Solid Dose Rotary",
                "Vinay Yadav (Formulation Lead)",
                "Active film coating operation currently running in Pan Coater 02."
        );
        b6 = batchRepository.save(b6);
        auditService.logEvent("BATCH", b6.getBatchNumber(), "CREATE_BATCH", "Vinay Yadav", "Process Chemist", "Routine Campaign", "Initiated campaign batch for Azithromycin.");

        ebrRepository.save(new ElectronicBatchRecord(b6, 1, "Active & Excipient Dispensing", BatchStage.DISPENSING,
                "Mass: 75.0 kg API, 35.0 kg Lactose", "Mass: 75.02 kg API, 34.98 kg Lactose", "P. Nair", "Vinay Yadav", "PASS", null, "Dual-sign verification completed."));
        ebrRepository.save(new ElectronicBatchRecord(b6, 2, "Wet Massing & Rapid Drying", BatchStage.GRANULATION,
                "Drying Temp: 55°C, Final LOD: 1.2-1.8%", "Drying Temp: 55°C, LOD: 1.45%", "P. Nair", "Vinay Yadav", "PASS", null, "LOD confirmed via moisture analyzer."));
        ebrRepository.save(new ElectronicBatchRecord(b6, 3, "Tablet Compression", BatchStage.COMPRESSION,
                "Target Weight: 750 mg +/- 3%", "Target Weight: 749 mg avg", "P. Nair", "Vinay Yadav", "PASS", null, "Friability: 0.12%, Hardness: 95 N."));
    }
}
