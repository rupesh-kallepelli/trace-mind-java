package com.hcltech.trace.mind.agent.service;

import java.util.List;

import com.hcltech.trace.mind.agent.entities.InvestigationEvidence;

public interface InvestigationEvidenceService {

    InvestigationEvidence save(InvestigationEvidence evidence);

    List<InvestigationEvidence> getByInvestigation(String investigationId);

}