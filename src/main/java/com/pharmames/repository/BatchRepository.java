package com.pharmames.repository;

import com.pharmames.model.Batch;
import com.pharmames.model.BatchStage;
import com.pharmames.model.BatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {

    Optional<Batch> findByBatchNumber(String batchNumber);

    List<Batch> findByStatusOrderByCreatedAtDesc(BatchStatus status);

    List<Batch> findByCurrentStageOrderByCreatedAtDesc(BatchStage stage);

    long countByStatus(BatchStatus status);

    @Query("SELECT b FROM Batch b WHERE " +
           "LOWER(b.batchNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.dosageForm) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.assignedLine) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY b.createdAt DESC")
    List<Batch> searchBatches(@Param("keyword") String keyword);

    List<Batch> findAllByOrderByCreatedAtDesc();
}
