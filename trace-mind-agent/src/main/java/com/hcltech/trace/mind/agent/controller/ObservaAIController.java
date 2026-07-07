package com.hcltech.trace.mind.agent.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/analyze")
    public ResponseEntity<String> analyze(
            @RequestParam String issue)
            throws Exception {
        log.info("REST request for AI investigation of issue: {}", issue);
        try {
            String investigationResult = aiInvestigationService.investigate(issue);
            log.debug("AI investigation analysis returned successfully");
            return ResponseEntity.ok(investigationResult);
        } catch (Exception e) {
            log.error("AI investigation failed for issue: {}. Error: {}", issue, e.getMessage(), e);
            throw e;
        }
    }
}