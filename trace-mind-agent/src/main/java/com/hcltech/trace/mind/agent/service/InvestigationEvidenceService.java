package com.hcltech.trace.mind.agent.service;

import java.util.List;
import java.util.UUID;

import com.hcltech.trace.mind.agent.entities.InvestigationEvidence;

public interface InvestigationEvidenceService {

    InvestigationEvidence save(InvestigationEvidence evidence);

    List<InvestigationEvidence> getByInvestigation(UUID investigationId);

}