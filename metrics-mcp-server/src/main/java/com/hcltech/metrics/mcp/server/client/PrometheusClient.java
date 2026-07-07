package com.hcltech.metrics.mcp.server.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class PrometheusClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${prometheus.url}")
    private String prometheusUrl;

    public JsonNode query(String promQl) {

        try {

            String response = restClient.get()
                    .uri(prometheusUrl + "/api/v1/query?query={query}", promQl)
                    .retrieve()
                    .body(String.class);

            return objectMapper.readTree(response);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to execute Prometheus query: " + promQl,
                    e);
        }
    }
}
