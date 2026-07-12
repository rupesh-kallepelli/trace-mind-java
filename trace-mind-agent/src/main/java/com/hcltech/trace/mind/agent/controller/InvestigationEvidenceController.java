package com.hcltech.trace.mind.agent.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcltech.trace.mind.agent.entities.InvestigationEvidence;
import com.hcltech.trace.mind.agent.service.InvestigationEvidenceService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/evidence")
@Slf4j
@CrossOrigin("*")
public class InvestigationEvidenceController {

    private final InvestigationEvidenceService service;

    @PostMapping
    public ResponseEntity<InvestigationEvidence> create(
            @RequestBody InvestigationEvidence request) {
        log.info("REST request to save investigation evidence");
        try {
            InvestigationEvidence saved = service.save(request);
            log.debug("Saved investigation evidence: {}", saved);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            log.error("Failed to save investigation evidence: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{investigationId}")
    public ResponseEntity<List<InvestigationEvidence>> get(
            @PathVariable String investigationId) {
        log.info("REST request to get evidence for investigation: {}", investigationId);
        try {
            List<InvestigationEvidence> results = service.getByInvestigation(investigationId);
            log.debug("Found {} evidence items for investigation: {}", results.size(), investigationId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Failed to retrieve evidence for investigation {}: {}", investigationId, e.getMessage(), e);
            throw e;
        }
    }
}