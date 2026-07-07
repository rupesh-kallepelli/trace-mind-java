package com.hcltech.trace.mind.agent.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hcltech.trace.mind.agent.entities.InvestigationEvidence;
import com.hcltech.trace.mind.agent.repository.InvestigationEvidenceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvestigationEvidenceServiceImpl
        implements InvestigationEvidenceService {

    private final InvestigationEvidenceRepository repository;

    @Override
    public InvestigationEvidence save(
            InvestigationEvidence evidence) {
        log.info("Saving new evidence for investigation ID: {}", evidence.getInvestigationId());
        evidence.setCreatedAt(LocalDateTime.now());

        InvestigationEvidence saved = repository.save(evidence);
        log.debug("Evidence saved with ID: {} and type: {}", saved.getId(), saved.getAgentType());
        return saved;
    }

    @Override
    public List<InvestigationEvidence>
    getByInvestigation(UUID investigationId) {
        log.debug("Retrieving all evidence for investigation: {}", investigationId);
        return repository.findByInvestigationId(
                investigationId);
    }
}