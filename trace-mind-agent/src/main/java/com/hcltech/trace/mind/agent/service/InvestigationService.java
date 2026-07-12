package com.hcltech.trace.mind.agent.service;

import org.springframework.data.domain.Page;

import com.hcltech.trace.mind.agent.request.CreateInvestigationRequest;
import com.hcltech.trace.mind.agent.response.InvestigationResponse;
import com.hcltech.trace.mind.agent.response.InvestigationSummaryResponse;

public interface InvestigationService {

        InvestigationResponse create(
                        CreateInvestigationRequest request);

        InvestigationResponse getById(String id);

        Page<InvestigationSummaryResponse> getAll(
                        int page,
                        int size);

        void updateResult(
                        String id,
                        String rootCause,
                        Double confidenceScore,
                        String reportMarkdown);

        void updateResult(
                        String id,
                        String serviceName,
                        String issueDescription,
                        String rootCause,
                        Double confidenceScore,
                        String reportMarkdown);

        void markRunning(String id);

        void markCompleted(String id);

        void markFailed(String id);
}