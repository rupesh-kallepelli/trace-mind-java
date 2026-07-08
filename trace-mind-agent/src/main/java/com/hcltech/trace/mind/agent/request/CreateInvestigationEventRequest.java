package com.hcltech.trace.mind.agent.request;

import com.hcltech.trace.mind.agent.entities.EventType;

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