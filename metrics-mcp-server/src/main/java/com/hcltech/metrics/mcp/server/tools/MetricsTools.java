package com.hcltech.metrics.mcp.server.tools;

import java.util.Map;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Service;

import com.hcltech.metrics.mcp.server.service.MetricsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsTools {

    private final MetricsService metricsService;

    @McpTool(name = "metric_latency", description = """
            Retrieve latency metrics for a service.

            Returns:
            - P50 latency
            - P95 latency
            - P99 latency
            - latency findings
            """)
    public Map<String, Object> metricLatency(
            String service,
            String namespace) {

        log.info("MCP Tool: metric_latency called for service: {} in namespace: {}", service, namespace);
        try {
            Map<String, Object> response = metricsService.investigateLatency(
                    service,
                    namespace);
            log.debug("MCP Tool: metric_latency result: {}", response);
            return response;
        } catch (Exception e) {
            log.error("MCP Tool: metric_latency failed for service: {}. Error: {}", service, e.getMessage(), e);
            throw e;
        }
    }

    @McpTool(name = "metric_error_rate", description = """
            Retrieve application error rates.

            Returns:
            - 4xx error rate
            - 5xx error rate
            - total error rate
            - findings
            """)
    public Map<String, Object> metricErrorRate(
            String service,
            String namespace) {

        log.info("MCP Tool: metric_error_rate called for service: {} in namespace: {}", service, namespace);
        try {
            Map<String, Object> response = metricsService.investigateErrorRate(
                    service,
                    namespace);
            log.debug("MCP Tool: metric_error_rate result: {}", response);
            return response;
        } catch (Exception e) {
            log.error("MCP Tool: metric_error_rate failed for service: {}. Error: {}", service, e.getMessage(), e);
            throw e;
        }
    }

    @McpTool(name = "metric_cpu_usage", description = """
            Retrieve CPU utilization metrics.
            """)
    public Map<String, Object> metricCpuUsage(
            String service,
            String namespace) {

        log.info("MCP Tool: metric_cpu_usage called for service: {} in namespace: {}", service, namespace);
        try {
            Map<String, Object> response = metricsService.investigateCpu(
                    service,
                    namespace);
            log.debug("MCP Tool: metric_cpu_usage result: {}", response);
            return response;
        } catch (Exception e) {
            log.error("MCP Tool: metric_cpu_usage failed for service: {}. Error: {}", service, e.getMessage(), e);
            throw e;
        }
    }

    @McpTool(name = "metric_memory_usage", description = """
            Retrieve memory utilization metrics.
            """)
    public Map<String, Object> metricMemoryUsage(
            String service,
            String namespace) {

        log.info("MCP Tool: metric_memory_usage called for service: {} in namespace: {}", service, namespace);
        try {
            Map<String, Object> response = metricsService.investigateMemory(
                    service,
                    namespace);
            log.debug("MCP Tool: metric_memory_usage result: {}", response);
            return response;
        } catch (Exception e) {
            log.error("MCP Tool: metric_memory_usage failed for service: {}. Error: {}", service, e.getMessage(), e);
            throw e;
        }
    }

    @McpTool(name = "metric_request_rate", description = """
            Retrieve request volume and throughput metrics.
            """)
    public Map<String, Object> metricRequestRate(
            String service,
            String namespace) {

        log.info("MCP Tool: metric_request_rate called for service: {} in namespace: {}", service, namespace);
        try {
            Map<String, Object> response = metricsService.investigateRequestRate(
                    service,
                    namespace);
            log.debug("MCP Tool: metric_request_rate result: {}", response);
            return response;
        } catch (Exception e) {
            log.error("MCP Tool: metric_request_rate failed for service: {}. Error: {}", service, e.getMessage(), e);
            throw e;
        }
    }

    @McpTool(name = "metric_restart_count", description = """
            Retrieve pod restart metrics.
            """)
    public Map<String, Object> metricRestartCount(
            String service,
            String namespace) {

        log.info("MCP Tool: metric_restart_count called for service: {} in namespace: {}", service, namespace);
        try {
            Map<String, Object> response = metricsService.investigateRestarts(
                    service,
                    namespace);
            log.debug("MCP Tool: metric_restart_count result: {}", response);
            return response;
        } catch (Exception e) {
            log.error("MCP Tool: metric_restart_count failed for service: {}. Error: {}", service, e.getMessage(), e);
            throw e;
        }
    }
}