package com.hcltech.traces.mcp.server.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ZipkinClient {

    private final RestClient restClient;

    @Value("${zipkin.url}")
    private String zipkinUrl;

    public JsonNode getTraces(String service) {

        String response = restClient.get()
                .uri(
                        zipkinUrl +
                                "/api/v2/traces?serviceName={service}&limit=100",
                        service)
                .retrieve()
                .body(String.class);

        try {
            return new ObjectMapper().readTree(response);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to parse Zipkin response",
                    e);
        }
    }
}