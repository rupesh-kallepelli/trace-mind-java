package com.hcltech.metrics.mcp.server.service;

import java.util.Map;

import com.hcltech.metrics.mcp.server.response.MetricsInvestigationResponse;

public interface MetricsService {

    Map<String, Object> investigateLatency(
            String service,
            String namespace);

    Map<String, Object> investigateErrorRate(
            String service,
            String namespace);

    Map<String, Object> investigateCpu(
            String service,
            String namespace);

    Map<String, Object> investigateMemory(
            String service,
            String namespace);

    Map<String, Object> investigateRequestRate(
            String service,
            String namespace);

    Map<String, Object> investigateRestarts(
            String service,
            String namespace);

    MetricsInvestigationResponse investigateMetrics(
            String service,
            String namespace);
}

