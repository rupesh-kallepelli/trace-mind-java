package com.hcltech.orchestrator_agent.repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcltech.orchestrator_agent.entity.Investigation;

@Repository
public interface InvestigationRepository extends JpaRepository<Investigation, String> {

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
