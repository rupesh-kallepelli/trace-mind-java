package com.hcltech.trace.mind.agent.response;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvidenceResponse {

    private UUID id;

    private String agentType;

    private String summary;

    private Map<String, Object> evidence;

    private LocalDateTime createdAt;
}