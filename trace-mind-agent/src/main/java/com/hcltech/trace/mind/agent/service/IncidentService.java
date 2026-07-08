package com.hcltech.trace.mind.agent.service;

import java.util.List;

import com.hcltech.trace.mind.agent.entities.Incident;
import com.hcltech.trace.mind.agent.response.IncidentResponse;

public interface IncidentService {

    List<IncidentResponse> getAll();

    IncidentResponse getById(String id);

    IncidentResponse resolve(String id);

    IncidentResponse assign(String id, String assignedTo);
    IncidentResponse create(Incident incident);
}