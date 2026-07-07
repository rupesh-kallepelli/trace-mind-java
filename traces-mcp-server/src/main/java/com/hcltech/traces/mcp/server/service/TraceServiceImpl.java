package com.hcltech.traces.mcp.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.traces.mcp.server.client.ZipkinClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraceServiceImpl implements TraceService {

    private final ZipkinClient zipkinClient;

    private final ObjectMapper objectMapper;

    @Override
    public Map<String, Object> traceSlowRequests(
            String service,
            String namespace) {

        log.info("Analyzing slow requests for service: {} in namespace: {}", service, namespace);
        try {
            JsonNode traces = zipkinClient.getTraces(service);

            long totalDuration = 0;
            long maxDuration = 0;
            int count = 0;

            for (JsonNode trace : traces) {
                long traceDuration = getTraceDuration(trace);
                totalDuration += traceDuration;
                maxDuration = Math.max(maxDuration, traceDuration);
                count++;
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("service", service);
            result.put("namespace", namespace);
            result.put("traceCount", count);
            result.put("averageDurationMs", count == 0 ? 0 : totalDuration / count / 1000.0);
            result.put("maxDurationMs", maxDuration / 1000.0);

            log.debug("Slow request analysis result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Failed to analyze slow requests for service: {}", service, e);
            throw new RuntimeException("Error analyzing slow traces", e);
        }
    }

    @Override
    public Map<String, Object> traceFailedRequests(
            String service,
            String namespace) {
        log.info("Analyzing failed requests for service: {} in namespace: {}", service, namespace);
        try {
            JsonNode traces = zipkinClient.getTraces(service);

            int failed = 0;
            List<String> errors = new ArrayList<>();

            for (JsonNode trace : traces) {
                for (JsonNode span : trace) {
                    JsonNode tags = span.path("tags");
                    if (tags.has("error")) {
                        failed++;
                        errors.add(tags.path("error").asText());
                    }
                }
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("service", service);
            result.put("namespace", namespace);
            result.put("failedTraceCount", failed);
            result.put("errors", errors);

            log.debug("Failed request analysis result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Failed to analyze failed requests for service: {}", service, e);
            throw new RuntimeException("Error analyzing failed traces", e);
        }
    }

    @Override
    public Map<String, Object> traceBottleneckAnalysis(
            String service,
            String namespace) {
        log.info("Identifying bottlenecks for service: {} in namespace: {}", service, namespace);
        try {
            JsonNode traces = zipkinClient.getTraces(service);

            String slowestSpan = null;
            long longestDuration = 0;

            for (JsonNode trace : traces) {
                for (JsonNode span : trace) {
                    long duration = span.path("duration").asLong();
                    if (duration > longestDuration) {
                        longestDuration = duration;
                        slowestSpan = span.path("name").asText();
                    }
                }
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("service", service);
            result.put("namespace", namespace);
            result.put("slowestSpan", slowestSpan);
            result.put("durationMs", longestDuration / 1000.0);

            log.debug("Bottleneck analysis result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Failed to analyze bottlenecks for service: {}", service, e);
            throw new RuntimeException("Error performing bottleneck analysis", e);
        }
    }

    @Override
    public Map<String, Object> traceDependencies(
            String service,
            String namespace) {
        log.info("Analyzing dependencies for service: {} in namespace: {}", service, namespace);
        try {
            JsonNode traces = zipkinClient.getTraces(service);

            Set<String> dependencies = new HashSet<>();

            for (JsonNode trace : traces) {
                for (JsonNode span : trace) {
                    JsonNode endpoint = span.path("localEndpoint");
                    String serviceName = endpoint.path("serviceName").asText();

                    if (!serviceName.isEmpty() && !service.equalsIgnoreCase(serviceName)) {
                        dependencies.add(serviceName);
                    }
                }
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("service", service);
            result.put("namespace", namespace);
            result.put("dependencies", dependencies);

            log.debug("Dependency analysis result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Failed to analyze dependencies for service: {}", service, e);
            throw new RuntimeException("Error analyzing service dependencies", e);
        }
    }

    @Override
    public Map<String, Object> traceErrorPropagation(
            String service,
            String namespace) {
        log.info("Analyzing error propagation for service: {} in namespace: {}", service, namespace);
        try {
            JsonNode traces = zipkinClient.getTraces(service);

            List<Map<String, String>> propagation = new ArrayList<>();

            for (JsonNode trace : traces) {
                for (JsonNode span : trace) {
                    JsonNode tags = span.path("tags");

                    if (tags.has("error")) {
                        Map<String, String> error = new LinkedHashMap<>();
                        error.put(
                                "span",
                                span.path("name").asText());
                        error.put(
                                "service",
                                span.path("localEndpoint").path("serviceName").asText());
                        error.put(
                                "error",
                                tags.path("error").asText());

                        propagation.add(error);
                    }
                }
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("service", service);
            result.put("namespace", namespace);
            result.put("errorPropagation", propagation);

            log.debug("Error propagation analysis result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Failed to analyze error propagation for service: {}", service, e);
            throw new RuntimeException("Error analyzing error propagation", e);
        }
    }

    @Override
    public Map<String, Object> traceCriticalPath(
            String service,
            String namespace) {
        log.info("Analyzing critical path for service: {} in namespace: {}", service, namespace);
        try {
            JsonNode traces = zipkinClient.getTraces(service);

            List<Map<String, Object>> criticalPath = new ArrayList<>();

            long maxDuration = 0;
            JsonNode slowestTrace = null;

            for (JsonNode trace : traces) {
                long duration = getTraceDuration(trace);
                if (duration > maxDuration) {
                    maxDuration = duration;
                    slowestTrace = trace;
                }
            }

            if (slowestTrace != null) {
                for (JsonNode span : slowestTrace) {
                    Map<String, Object> spanInfo = new LinkedHashMap<>();
                    spanInfo.put(
                            "span",
                            span.path("name").asText());
                    spanInfo.put(
                            "service",
                            span.path("localEndpoint").path("serviceName").asText());
                    spanInfo.put(
                            "durationMs",
                            span.path("duration").asLong() / 1000.0);

                    criticalPath.add(spanInfo);
                }
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("service", service);
            result.put("namespace", namespace);
            result.put("criticalPath", criticalPath);

            log.debug("Critical path analysis result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Failed to analyze critical path for service: {}", service, e);
            throw new RuntimeException("Error analyzing critical path", e);
        }
    }

    private long getTraceDuration(JsonNode trace) {
        long duration = 0;
        for (JsonNode span : trace) {
            duration += span.path("duration").asLong();
        }
        return duration;
    }
}