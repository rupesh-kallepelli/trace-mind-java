package com.hcltech.trace.mind.agent.service;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcltech.trace.mind.agent.entities.Incident;
import com.hcltech.trace.mind.agent.repository.IncidentRepository;
import com.hcltech.trace.mind.agent.response.IncidentResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class IncidentServiceImpl implements IncidentService {

        private final IncidentRepository incidentRepository;

        @Override
        @Transactional(readOnly = true)
        public List<IncidentResponse> getAll() {
                log.debug("Service: Retrieving all incidents from repository");
                return incidentRepository.findAll()
                                .stream()
                                .map(this::mapToResponse)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public IncidentResponse getById(String id) {
                log.debug("Service: Retrieving incident by ID: {}", id);
                Incident incident = incidentRepository.findById(id)
                                .orElseThrow(() -> {
                                    log.error("Incident not found: {}", id);
                                    return new EntityNotFoundException("Incident not found: " + id);});

                return mapToResponse(incident);
        }

        @Override
        public IncidentResponse resolve(String id) {
                log.info("Service: Resolving incident ID: {}", id);
                Incident incident = incidentRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Incident not found: " + id));

                incident.setStatus("RESOLVED");
                incident.setResolvedAt(LocalDateTime.now());

                Incident saved = incidentRepository.save(incident);
                log.debug("Incident {} resolved successfully", id);
                return mapToResponse(saved);
        }

        @Override
        public IncidentResponse assign(
                        String id,
                        String assignedTo) {
                log.info("Service: Assigning incident ID: {} to user: {}", id, assignedTo);
                Incident incident = incidentRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Incident not found: " + id));

                incident.setAssignedTo(assignedTo);

                Incident saved = incidentRepository.save(incident);
                log.debug("Incident {} assignment updated in DB", id);
                return mapToResponse(saved);
        }

        private IncidentResponse mapToResponse(
                        Incident incident) {

                return IncidentResponse.builder()
                                .id(incident.getId())
                                .incidentNumber(incident.getIncidentNumber())
                                .severity(incident.getSeverity())
                                .status(incident.getStatus())
                                .assignedTo(incident.getAssignedTo())
                                .investigationId(incident.getInvestigationId())
                                .createdAt(incident.getCreatedAt())
                                .resolvedAt(incident.getResolvedAt())
                                .build();
        }

        @Override
        public IncidentResponse create(
                        Incident incident) {
                log.info("Service: Creating new incident record: {}", incident.getIncidentNumber());
                Incident saved = incidentRepository.save(incident);
                log.debug("Incident saved with ID: {}", saved.getId());
                return mapToResponse(saved);
        }
}