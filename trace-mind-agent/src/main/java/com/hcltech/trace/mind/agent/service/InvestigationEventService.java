package com.hcltech.trace.mind.agent.service;

import java.util.List;

import com.hcltech.trace.mind.agent.entities.EventType;
import com.hcltech.trace.mind.agent.entities.InvestigationEvent;
import com.hcltech.trace.mind.agent.response.InvestigationEventResponse;

public interface InvestigationEventService {

    InvestigationEvent publish(
            String investigationId,
            EventType eventType,
            String source,
            String message,
            String payload);

    List<InvestigationEventResponse> getEvents(String investigationId);
}