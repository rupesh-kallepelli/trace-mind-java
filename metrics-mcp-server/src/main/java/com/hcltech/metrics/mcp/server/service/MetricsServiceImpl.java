package com.hcltech.metrics.mcp.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hcltech.metrics.mcp.server.client.PrometheusClient;
import com.hcltech.metrics.mcp.server.response.MetricsInvestigationResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsServiceImpl implements MetricsService {

  private final PrometheusClient prometheusClient;

  @Override
  public MetricsInvestigationResponse investigateMetrics(
      String service,
      String namespace) {

    log.info("Starting metrics investigation for service: {} in namespace: {}", service, namespace);

    Double cpu = getCpuUsage(service);
    Double memory = getMemoryUsage(service);
    Double errorRate = getErrorRate(service);
    Double requestRate = getRequestRate(service);
    Double latency = getLatency(service);
    Integer restarts = getRestarts(service);

    MetricsInvestigationResponse response = MetricsInvestigationResponse.builder()
        .service(service)
        .cpuUsage(cpu)
        .memoryUsageMb(memory)
        .errorRate(errorRate)
        .requestRate(requestRate)
        .p95LatencyMs(latency)
        .restartCount(restarts)
        .findings(generateFindings(
            cpu,
            memory,
            errorRate,
            latency,
            restarts))
        .build();

    log.info("Investigation complete for service: {}. Found {} findings.", service, response.getFindings().size());
    log.debug("Full investigation result: {}", response);
    return response;
  }

  private Double getCpuUsage(String service) {

    String promQl = """
        sum(
          rate(
            container_cpu_usage_seconds_total{
              pod=~".*%s.*"
            }[5m]
          )
        )
        """.formatted(service);

    return getDoubleValue(promQl);
  }

  private Double getMemoryUsage(String service) {

    String promQl = """
        sum(
          container_memory_working_set_bytes{
            pod=~".*%s.*"
          }
        ) / 1024 / 1024
        """.formatted(service);

    return getDoubleValue(promQl);
  }

  private Integer getRestarts(String service) {

    String promQl = """
        sum(
          kube_pod_container_status_restarts_total{
             pod=~".*%s.*"
          }
        )
        """.formatted(service);

    return getDoubleValue(promQl).intValue();
  }

  private Double getRequestRate(String service) {

    String promQl = """
        sum(
          rate(
            http_server_requests_seconds_count{
               application="%s"
            }[5m]
          )
        )
        """.formatted(service);

    return getDoubleValue(promQl);
  }

  private Double getErrorRate(String service) {

    String errorQuery = """
        sum(
         rate(
           http_server_requests_seconds_count{
             application="%s",
             status=~"5.."
           }[5m]
         )
        )
        """.formatted(service);

    String totalQuery = """
        sum(
         rate(
          http_server_requests_seconds_count{
            application="%s"
          }[5m]
         )
        )
        """.formatted(service);

    Double errors = getDoubleValue(errorQuery);
    Double total = getDoubleValue(totalQuery);

    if (total == null || total == 0.0d) {
      log.debug("Total requests for service {} is zero, error rate set to 0.0", service);
      return 0.0d;
    }

    double rate = (errors / total) * 100;
    log.debug("Calculated error rate for {}: {}% (Errors: {}, Total: {})", service, rate, errors, total);
    return rate;
  }

  private Double getLatency(String service) {

    String promQl = """
        histogram_quantile(
          0.95,
          sum(
            rate(
              http_server_requests_seconds_bucket{
                application="%s"
              }[5m]
            )
          ) by (le)
        ) * 1000
        """.formatted(service);

    return getDoubleValue(promQl);
  }

  private Double getDoubleValue(String query) {
    log.debug("Executing PromQL query: {}", query);
    try {
      JsonNode response = prometheusClient.query(query);

      JsonNode result = response.path("data")
          .path("result");

      if (result.isEmpty()) {
        log.debug("Prometheus returned empty result for query: {}", query);
        return 0.0d;
      }

      String valueStr = result.get(0).path("value").get(1).asText();
      Double value = Double.parseDouble(valueStr);
      log.debug("PromQL result value: {}", value);
      return value;
    } catch (Exception e) {
      log.error("Failed to fetch or parse metric from Prometheus. Query: {}. Error: {}", query, e.getMessage(), e);
      return 0.0d;
    }
  }

  private List<String> generateFindings(
      Double cpu,
      Double memory,
      Double errorRate,
      Double latency,
      Integer restarts) {

    List<String> findings = new ArrayList<>();

    if (cpu > 0.8) {
      findings.add("High CPU utilization detected");
    }

    if (memory > 2048) {
      findings.add("High memory consumption detected");
    }

    if (errorRate > 5) {
      findings.add("High application error rate detected");
    }

    if (latency > 2000) {
      findings.add("High latency detected");
    }

    if (restarts > 0) {
      findings.add("Container restarts detected");
    }

    if (findings.isEmpty()) {
      findings.add("No major metric anomalies detected");
    }

    return findings;
  }

  @Override
  public Map<String, Object> investigateLatency(
      String service,
      String namespace) {

    log.info("Starting specialized latency investigation for service: {}", service);

    Map<String, Object> response = new LinkedHashMap<>();

    Double latency = getLatency(service);

    response.put("service", service);
    response.put("namespace", namespace);
    response.put("p95LatencyMs", latency);

    if (latency > 2000) {
      response.put("finding", "High latency detected");
      response.put("severity", "HIGH");
    } else {
      response.put("finding", "Latency within acceptable range");
      response.put("severity", "LOW");
    }

    log.debug("Latency investigation result: {}", response);
    return response;
  }

  @Override
  public Map<String, Object> investigateErrorRate(
      String service,
      String namespace) {

    log.info("Starting specialized error rate investigation for service: {}", service);

    Map<String, Object> response = new LinkedHashMap<>();

    Double errorRate = getErrorRate(service);

    response.put("service", service);
    response.put("namespace", namespace);
    response.put("errorRate", errorRate);

    if (errorRate > 5) {
      response.put("finding", "High error rate detected");
      response.put("severity", "HIGH");
    } else {
      response.put("finding", "Error rate normal");
      response.put("severity", "LOW");
    }

    log.debug("Error rate investigation result: {}", response);
    return response;
  }

  @Override
  public Map<String, Object> investigateCpu(
      String service,
      String namespace) {

    log.info("Starting specialized CPU investigation for service: {}", service);

    Map<String, Object> response = new LinkedHashMap<>();

    Double cpu = getCpuUsage(service);

    response.put("service", service);
    response.put("namespace", namespace);
    response.put("cpuUsage", cpu);

    if (cpu > 0.8) {
      response.put("finding", "CPU saturation detected");
      response.put("severity", "HIGH");
    } else {
      response.put("finding", "CPU utilization healthy");
      response.put("severity", "LOW");
    }

    log.debug("CPU investigation result: {}", response);
    return response;
  }

  @Override
  public Map<String, Object> investigateMemory(
      String service,
      String namespace) {

    log.info("Starting specialized memory investigation for service: {}", service);

    Map<String, Object> response = new LinkedHashMap<>();

    Double memory = getMemoryUsage(service);

    response.put("service", service);
    response.put("namespace", namespace);
    response.put("memoryUsageMb", memory);

    if (memory > 2048) {
      response.put("finding", "High memory consumption detected");
      response.put("severity", "MEDIUM");
    } else {
      response.put("finding", "Memory utilization healthy");
      response.put("severity", "LOW");
    }

    log.debug("Memory investigation result: {}", response);
    return response;
  }

  @Override
  public Map<String, Object> investigateRequestRate(
      String service,
      String namespace) {

    log.info("Starting specialized request rate investigation for service: {}", service);

    Map<String, Object> response = new LinkedHashMap<>();

    Double requestRate = getRequestRate(service);

    response.put("service", service);
    response.put("namespace", namespace);
    response.put("requestRate", requestRate);

    response.put(
        "finding",
        requestRate > 1000
            ? "Traffic spike detected"
            : "Normal traffic levels");

    log.debug("Request rate investigation result: {}", response);
    return response;
  }

  @Override
  public Map<String, Object> investigateRestarts(
      String service,
      String namespace) {

    log.info("Starting specialized restart investigation for service: {}", service);

    Map<String, Object> response = new LinkedHashMap<>();

    Integer restarts = getRestarts(service);

    response.put("service", service);
    response.put("namespace", namespace);
    response.put("restartCount", restarts);

    if (restarts > 0) {
      response.put("finding", "Pod/container restarts detected");
      response.put("severity", "MEDIUM");
    } else {
      response.put("finding", "No restart anomalies detected");
      response.put("severity", "LOW");
    }

    log.debug("Restart investigation result: {}", response);
    return response;
  }
}
