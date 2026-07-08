package com.hcltech.trace.mind.agent.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcltech.trace.mind.agent.entities.Investigation;
import com.hcltech.trace.mind.agent.repository.InvestigationRepository;
import com.hcltech.trace.mind.agent.request.CreateInvestigationRequest;
import com.hcltech.trace.mind.agent.response.InvestigationResponse;
import com.hcltech.trace.mind.agent.response.InvestigationSummaryResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InvestigationServiceImpl implements InvestigationService {

        private final InvestigationRepository investigationRepository;

        @Override
        public InvestigationResponse create(CreateInvestigationRequest request) {
                log.info("Creating new investigation for service: {}", request.getServiceName());

                Investigation investigation = Investigation.builder()
                                .serviceName(request.getServiceName())
                                .namespace(request.getNamespace())
                                .issueDescription(request.getIssueDescription())
                                .status("CREATED")
                                .startedAt(LocalDateTime.now())
                                .build();

                Investigation saved = investigationRepository.save(investigation);

                log.debug("Saved investigation with ID: {}", saved.getId());
                return mapToResponse(saved);
        }

        @Override
        @Transactional(readOnly = true)
        public InvestigationResponse getById(String id) {
                log.debug("Retrieving investigation details for ID: {}", id);
                Investigation investigation = investigationRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Investigation not found: " + id));

                return mapToResponse(investigation);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<InvestigationSummaryResponse> getAll(
                        int page,
                        int size) {
                log.debug("Fetching investigations page: {}, size: {}", page, size);
                return investigationRepository
                                .findAll(PageRequest.of(page, size))
                                .map(this::mapToSummaryResponse);
        }

        @Override
        public void updateResult(
                        String id,
                        String rootCause,
                        Double confidenceScore,
                        String reportMarkdown) {
                log.info("Updating result for investigation: {}", id);
                Investigation investigation = investigationRepository.findById(id)
                                .orElseThrow(() -> {
                                        log.error("Update failed. Investigation not found: {}", id);
                                        return new EntityNotFoundException(
                                                        "Investigation not found: " + id);
                                });

                investigation.setRootCause(rootCause);
                investigation.setConfidenceScore(confidenceScore);
                investigation.setReportMarkdown(reportMarkdown);

                investigationRepository.save(investigation);
        }

        @Override
        public void updateResult(
                        String id,
                        String serviceName,
                        String issueDescription,
                        String rootCause,
                        Double confidenceScore,
                        String reportMarkdown) {

                log.info("Updating investigation result for {}", id);

                Investigation investigation = investigationRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Investigation not found: " + id));

                if (serviceName != null && !serviceName.isBlank()) {
                        investigation.setServiceName(serviceName);
                }

                if (issueDescription != null && !issueDescription.isBlank()) {
                        investigation.setIssueDescription(issueDescription);
                }

                investigation.setRootCause(rootCause);
                investigation.setConfidenceScore(confidenceScore);
                investigation.setReportMarkdown(reportMarkdown);

                investigationRepository.save(investigation);
        }

        @Override
        public void markRunning(String id) {
                log.debug("Marking investigation as RUNNING: {}", id);
                Investigation investigation = investigationRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Investigation not found: " + id));

                investigation.setStatus("RUNNING");
                investigationRepository.save(investigation);
        }

        @Override
        public void markCompleted(String id) {
                log.info("Marking investigation as COMPLETED: {}", id);
                Investigation investigation = investigationRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Investigation not found: " + id));

                investigation.setStatus("COMPLETED");
                investigation.setCompletedAt(LocalDateTime.now());
                investigationRepository.save(investigation);
        }

        @Override
        public void markFailed(String id) {
                log.error("Marking investigation as FAILED: {}", id);
                Investigation investigation = investigationRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Investigation not found: " + id));

                investigation.setStatus("FAILED");
                investigation.setCompletedAt(LocalDateTime.now());
                investigationRepository.save(investigation);
        }

        private InvestigationResponse mapToResponse(
                        Investigation investigation) {

                return InvestigationResponse.builder()
                                .id(investigation.getId())
                                .serviceName(investigation.getServiceName())
                                .namespace(investigation.getNamespace())
                                .issueDescription(investigation.getIssueDescription())
                                .status(investigation.getStatus())
                                .rootCause(investigation.getRootCause())
                                .confidenceScore(investigation.getConfidenceScore())
                                .reportMarkdown(investigation.getReportMarkdown())
                                .startedAt(investigation.getStartedAt())
                                .completedAt(investigation.getCompletedAt())
                                .build();
        }

        private InvestigationSummaryResponse mapToSummaryResponse(
                        Investigation investigation) {

                return InvestigationSummaryResponse.builder()
                                .id(investigation.getId())
                                .serviceName(investigation.getServiceName())
                                .namespace(investigation.getNamespace())
                                .status(investigation.getStatus())
                                .confidenceScore(investigation.getConfidenceScore())
                                .startedAt(investigation.getStartedAt())
                                .build();
        }
}