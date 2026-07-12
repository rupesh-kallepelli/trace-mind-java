package com.hcltech.trace.mind.agent.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcltech.trace.mind.agent.entities.Investigation;

@Repository
public interface InvestigationRepository
        extends JpaRepository<Investigation, String> {

    List<Investigation> findByServiceName(String serviceName);

    List<Investigation> findByStatus(String status);

    Page<Investigation> findAll(Pageable pageable);
     @Query("""
            SELECT i
            FROM Investigation i
            WHERE i.namespace = :namespace
            AND i.status = 'COMPLETED'
            AND i.startedAt >= :cutoff
            ORDER BY i.startedAt DESC
            """)
    List<Investigation> findRecentCompletedInvestigations(
            @Param("namespace") String namespace,
            @Param("cutoff") LocalDateTime cutoff);
}
