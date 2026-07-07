package com.hcltech.traces.mcp.server.tools;

import java.util.Map;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Service;

import com.hcltech.traces.mcp.server.service.TraceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class TraceTools {

    private final TraceService traceService;

    @McpTool(
            name = "trace_slow_requests",
            description = "Analyze slow traces and latency contributors"
    )
    public Map<String, Object> traceSlowRequests(
            String service,
            String namespace) {

        return traceService.traceSlowRequests(
                service,
                namespace);
    }

    @McpTool(
            name = "trace_failed_requests",
            description = "Analyze failing traces"
    )
    public Map<String, Object> traceFailedRequests(
            String service,
            String namespace) {

        return traceService.traceFailedRequests(
                service,
                namespace);
    }

    @McpTool(
            name = "trace_bottleneck_analysis",
            description = "Identify latency bottlenecks"
    )
    public Map<String, Object> traceBottleneckAnalysis(
            String service,
            String namespace) {

        return traceService.traceBottleneckAnalysis(
                service,
                namespace);
    }

    @McpTool(
            name = "trace_dependencies",
            description = "Analyze service dependencies"
    )
    public Map<String, Object> traceDependencies(
            String service,
            String namespace) {

        return traceService.traceDependencies(
                service,
                namespace);
    }

    @McpTool(
            name = "trace_error_propagation",
            description = "Analyze error propagation"
    )
    public Map<String, Object> traceErrorPropagation(
            String service,
            String namespace) {

        return traceService.traceErrorPropagation(
                service,
                namespace);
    }

    @McpTool(
            name = "trace_critical_path",
            description = "Analyze trace critical path"
    )
    public Map<String, Object> traceCriticalPath(
            String service,
            String namespace) {

        return traceService.traceCriticalPath(
                service,
                namespace);
    }
}