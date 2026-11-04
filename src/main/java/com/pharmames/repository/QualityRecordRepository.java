package com.pharmames.repository;

import com.pharmames.model.Batch;
import com.pharmames.model.QualityRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QualityRecordRepository extends JpaRepository<QualityRecord, Long> {

    List<QualityRecord> findByBatchOrderByReviewedAtDesc(Batch batch);

    List<QualityRecord> findByBatchIdOrderByReviewedAtDesc(Long batchId);

    List<QualityRecord> findAllByOrderByReviewedAtDesc();

    List<QualityRecord> findByDispositionOrderByReviewedAtDesc(String disposition);

    long countByDisposition(String disposition);
}
