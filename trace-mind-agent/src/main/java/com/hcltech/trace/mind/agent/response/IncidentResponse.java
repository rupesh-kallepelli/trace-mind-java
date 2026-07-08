package com.hcltech.trace.mind.agent.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentResponse {

    private String id;

    private String incidentNumber;

    private String severity;

    private String status;

    private String assignedTo;

    private String investigationId;

    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;
}