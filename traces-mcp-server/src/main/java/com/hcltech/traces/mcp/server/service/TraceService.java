package com.hcltech.traces.mcp.server.service;

import java.util.Map;
public interface TraceService {

    Map<String, Object> traceSlowRequests(
            String service,
            String namespace);

    Map<String, Object> traceFailedRequests(
            String service,
            String namespace);

    Map<String, Object> traceBottleneckAnalysis(
            String service,
            String namespace);

    Map<String, Object> traceDependencies(
            String service,
            String namespace);

    Map<String, Object> traceErrorPropagation(
            String service,
            String namespace);

    Map<String, Object> traceCriticalPath(
            String service,
            String namespace);
}