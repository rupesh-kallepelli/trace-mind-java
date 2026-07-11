package com.hcltech.orchestrator_agent.agents;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import com.hcltech.orchestrator_agent.entity.Investigation;
import com.hcltech.orchestrator_agent.repository.InvestigationRepository;
import com.hcltech.orchestrator_agent.request.EventType;
import com.hcltech.orchestrator_agent.service.TraceMindEventClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecentInvestigationRetrieverTools {

    private final InvestigationRepository investigationRepository;
    private final TraceMindEventClient eventClient;

    @McpTool(
        name = "retrieve_recent_investigations",
        description = """
        Retrieve similar investigations from last hour.
        """
    )
    public String retrieveRecentInvestigations(
            @McpToolParam(description = "investigationId")
            String investigationId,

            @McpToolParam(description = "Issue description")
            String issueDescription,

            @McpToolParam(description = "Namespace")
            String namespace) {

        log.info(
                "MCP Tool: retrieve_recent_investigations called for issue [{}] in namespace [{}]",
                issueDescription,
                namespace);

        eventClient.publishEvent(
                investigationId,
                EventType.AGENT_STARTED,
                "CONTEXT_AGENT",
                "Searching recent investigations",
                null);

        try {

          LocalDateTime cutoff = LocalDateTime.now().minusHours(1);
            log.debug("Searching for completed investigations in namespace '{}' since '{}'", namespace, cutoff);

            List<Investigation> investigations =
                    investigationRepository
                            .findRecentCompletedInvestigations(
                                    namespace,
                                    cutoff);

            log.info(
                    "Found {} recent completed investigations in namespace '{}'",
                    investigations.size(),
                    namespace);

            String result = investigations.stream()
                    .map(this::formatInvestigation)
                    .collect(Collectors.joining("\n\n"));

            eventClient.publishEvent(
                    investigationId,
                    EventType.AGENT_COMPLETED,
                    "CONTEXT_AGENT",
                    "Historical context found",
                    result);

            log.debug("MCP Tool: retrieve_recent_investigations result: {}", result);
            return result;

        } catch (Exception ex) {

            log.error(
                    "MCP Tool: retrieve_recent_investigations failed for issue [{}], namespace [{}]: {}",
                    issueDescription,
                    namespace,
                    ex.getMessage(), ex);
            eventClient.publishEvent(
                    investigationId,
                    EventType.AGENT_FAILED,
                    "CONTEXT_AGENT",
                    "Historical context retrieval failed",
                    ex.getMessage());

            throw ex;
        }
    }

    private String formatInvestigation(Investigation inv) {
        return """
                InvestigationId: %s
                Service: %s
                Issue: %s
                RootCause: %s
                Status: %s
                StartedAt: %s
                """
                .formatted(
                        inv.getId(),
                        inv.getServiceName(),
                        inv.getIssueDescription(),
                        inv.getRootCause(),
                        inv.getStatus(),
                        inv.getStartedAt());
    }
}