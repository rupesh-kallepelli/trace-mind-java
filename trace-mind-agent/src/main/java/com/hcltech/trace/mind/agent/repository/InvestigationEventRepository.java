package com.hcltech.trace.mind.agent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcltech.trace.mind.agent.entities.InvestigationEvent;

@Repository
public interface InvestigationEventRepository
        extends JpaRepository<InvestigationEvent, String> {

    List<InvestigationEvent> findByInvestigationIdOrderByCreatedAtAsc(String investigationId);
}