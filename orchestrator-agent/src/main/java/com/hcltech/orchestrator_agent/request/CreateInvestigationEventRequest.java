package com.hcltech.orchestrator_agent.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInvestigationEventRequest {

    private EventType eventType;

    private String source;

    private String message;

    private String payload;
}