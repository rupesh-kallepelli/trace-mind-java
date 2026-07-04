package com.hcltech.orchestrator_agent.response;

public record Evidence(String timestamp,
        String application,
        String correlationId,
        String exception,
        String message) {
}
