package com.hcltech.trace.mind.agent.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvestigationSummaryResponse {

    private String id;

    private String serviceName;

    private String namespace;

    private String status;

    private Double confidenceScore;

    private LocalDateTime startedAt;
}