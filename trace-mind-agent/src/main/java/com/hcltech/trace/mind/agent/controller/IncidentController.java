package com.hcltech.trace.mind.agent.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcltech.trace.mind.agent.response.IncidentResponse;
import com.hcltech.trace.mind.agent.service.IncidentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class IncidentController {

    private final IncidentService incidentService;

    @GetMapping
    public ResponseEntity<List<IncidentResponse>> getAll() {
        log.info("REST request to get all incidents");
        try {
            List<IncidentResponse> results = incidentService.getAll();
            log.debug("Found {} incidents", results.size());
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Failed to retrieve incidents: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> getById(
            @PathVariable String id) {
        log.info("REST request to get incident by ID: {}", id);
        try {
            IncidentResponse result = incidentService.getById(id);
            log.debug("Incident details retrieved: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to find incident with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<IncidentResponse> resolve(
            @PathVariable String id) {
        log.info("REST request to resolve incident: {}", id);
        try {
            IncidentResponse result = incidentService.resolve(id);
            log.info("Successfully resolved incident: {}", id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to resolve incident {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}/assign/{assignee}")
    public ResponseEntity<IncidentResponse> assign(
            @PathVariable String id,
            @PathVariable String assignee) {
        log.info("REST request to assign incident: {} to user: {}", id, assignee);
        try {
            IncidentResponse result = incidentService.assign(id, assignee);
            log.info("Successfully assigned incident {} to {}", id, assignee);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to assign incident {} to {}: {}", id, assignee, e.getMessage(), e);
            throw e;
        }
    }
}