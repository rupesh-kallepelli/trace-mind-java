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
public class InvestigationResponse {

    private UUID id;

    private String serviceName;

    private String namespace;

    private String issueDescription;

    private String status;

    private String rootCause;

    private Double confidenceScore;

    private String reportMarkdown;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;
}