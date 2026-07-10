package com.hcltech.trace.mind.agent.service;

import com.hcltech.trace.mind.agent.response.InvestigationResponse;

public interface AiInvestigationService {

    void executeInvestigation(
            String investigationId,
            String issue);

    InvestigationResponse createInvestigation(String issue);

}