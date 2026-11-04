package com.pharmames.repository;

import com.pharmames.model.Batch;
import com.pharmames.model.ElectronicBatchRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectronicBatchRecordRepository extends JpaRepository<ElectronicBatchRecord, Long> {

    List<ElectronicBatchRecord> findByBatchOrderByStepNumberAsc(Batch batch);

    List<ElectronicBatchRecord> findByBatchIdOrderByStepNumberAsc(Long batchId);

    List<ElectronicBatchRecord> findAllByOrderBySignedAtDesc();

    List<ElectronicBatchRecord> findTop10ByOrderBySignedAtDesc();
}
