package com.hcltech.trace.mind.agent.service;

import java.util.List;
import java.util.UUID;

import com.hcltech.trace.mind.agent.entities.Incident;
import com.hcltech.trace.mind.agent.response.IncidentResponse;

public interface IncidentService {

    List<IncidentResponse> getAll();

    IncidentResponse getById(UUID id);

    IncidentResponse resolve(UUID id);

    IncidentResponse assign(UUID id, String assignedTo);
    IncidentResponse create(Incident incident);
}