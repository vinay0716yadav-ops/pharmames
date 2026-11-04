package com.pharmames.repository;

import com.pharmames.model.AuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditTrailRepository extends JpaRepository<AuditTrail, Long> {

    List<AuditTrail> findAllByOrderByTimestampDesc();

    List<AuditTrail> findTop15ByOrderByTimestampDesc();

    List<AuditTrail> findByEntityIdentifierOrderByTimestampDesc(String entityIdentifier);

    List<AuditTrail> findByEntityTypeOrderByTimestampDesc(String entityType);

    @Query("SELECT a FROM AuditTrail a WHERE " +
           "LOWER(a.entityIdentifier) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.performedBy) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.action) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.details) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY a.timestamp DESC")
    List<AuditTrail> searchAuditLogs(@Param("query") String query);
}
