package com.hcltech.metrics.mcp.server.service;

import com.hcltech.metrics.mcp.server.response.MetricsInvestigationResponse;

public interface MetricsService {

    MetricsInvestigationResponse investigateMetrics(String service, String namespace);
}

