package com.hcltech.trace.mind.agent.response;

import java.time.Instant;
import java.util.UUID;

import com.hcltech.trace.mind.agent.entities.EventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationEventResponse {

    private String id;

    private String investigationId;

    private EventType eventType;

    private String source;

    private String message;

    private String payload;

    private Instant createdAt;
}