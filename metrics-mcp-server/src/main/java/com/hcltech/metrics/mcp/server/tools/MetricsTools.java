package com.hcltech.metrics.mcp.server.tools;

import com.hcltech.metrics.mcp.server.response.MetricsInvestigationResponse;
import com.hcltech.metrics.mcp.server.service.MetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsTools {

    private final MetricsService metricsService;

    @Tool(
            name = "investigate_metrics",
            description = """
            Analyze service metrics from Prometheus.
            
            Returns:
            - CPU usage
            - Memory usage
            - Request rate
            - Error rate
            - P95 latency
            - Restarts
            - Significant findings
            """
    )
    public MetricsInvestigationResponse investigateMetrics(
            String service,
            String namespace) {

        log.info("MCP Tool: investigate_metrics called for service: {} in namespace: {}", service, namespace);
        try {
            MetricsInvestigationResponse response = metricsService
                    .investigateMetrics(service, namespace);
            log.debug("MCP Tool: investigate_metrics successful for service: {}", service);
            return response;
        } catch (Exception e) {
            log.error("MCP Tool: investigate_metrics failed for service: {}. Error: {}", service, e.getMessage(), e);
            throw e;
        }
    }
}