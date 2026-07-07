package com.hcltech.trace.mind.agent.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.hcltech.trace.mind.agent.request.CreateInvestigationRequest;
import com.hcltech.trace.mind.agent.response.InvestigationResponse;
import com.hcltech.trace.mind.agent.response.InvestigationSummaryResponse;

public interface InvestigationService {

    InvestigationResponse create(
            CreateInvestigationRequest request);

    InvestigationResponse getById(UUID id);

    Page<InvestigationSummaryResponse> getAll(
            int page,
            int size);

    void updateResult(
            UUID id,
            String rootCause,
            Double confidenceScore,
            String reportMarkdown);

    void markRunning(UUID id);

    void markCompleted(UUID id);

    void markFailed(UUID id);
}