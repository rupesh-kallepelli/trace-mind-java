package com.hcltech.trace.mind.agent.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcltech.trace.mind.agent.entities.InvestigationEvidence;

@Repository
public interface InvestigationEvidenceRepository
        extends JpaRepository<InvestigationEvidence, UUID> {

    List<InvestigationEvidence> findByInvestigationId(UUID investigationId);
}