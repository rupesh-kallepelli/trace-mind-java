package com.hcltech.metrics.mcp.server.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MetricsInvestigationResponse {

    private String service;

    private Double cpuUsage;

    private Double memoryUsageMb;

    private Double errorRate;

    private Double requestRate;

    private Double p95LatencyMs;

    private Integer restartCount;

    private List<String> findings;
}