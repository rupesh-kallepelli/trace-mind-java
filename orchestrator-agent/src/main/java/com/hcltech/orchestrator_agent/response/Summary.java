package com.hcltech.orchestrator_agent.response;

import java.util.List;

public record Summary(
        long totalLogs,
        long errorLogs,
        List<String> applications,
        List<String> correlationIds
) {
}
