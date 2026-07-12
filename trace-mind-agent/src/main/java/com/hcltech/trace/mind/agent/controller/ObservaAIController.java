package com.hcltech.trace.mind.agent.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcltech.trace.mind.agent.response.InvestigationResponse;
import com.hcltech.trace.mind.agent.service.AiInvestigationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/observai")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class ObservaAIController {

    private final AiInvestigationService aiInvestigationService;

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(
            @RequestParam String issue, @RequestParam String email) {
        log.info("REST request to trigger AI analysis for issue: {}", issue);
        try {
            InvestigationResponse investigation = aiInvestigationService
                    .createInvestigation(issue, email);

            log.debug("Investigation created with ID: {}", investigation.getId());

            aiInvestigationService.executeInvestigation(investigation.getId(), issue);

            return ResponseEntity.accepted()
                    .body(
                            Map.of(
                                    "investigationId",
                                    investigation.getId(),
                                    "status",
                                    "STARTED"));
        } catch (Exception e) {
            log.error("Failed to start AI investigation for issue: {}", issue, e);
            throw e;
        }
    }
}