package com.hcltech.trace.mind.agent.tools;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.hcltech.trace.mind.agent.entities.EventType;
import com.hcltech.trace.mind.agent.entities.Investigation;
import com.hcltech.trace.mind.agent.repository.InvestigationRepository;
import com.hcltech.trace.mind.agent.service.InvestigationEventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecentInvestigationRetrieverTools {

        private final InvestigationRepository investigationRepository;
        private final InvestigationEventService eventService;

        @Tool(name = "retrieve_recent_investigations", description = """
                        Retrieve similar investigations from last hour.
                        """)
        public String retrieveRecentInvestigations(
                        @ToolParam(description = "investigationId") String investigationId,

                        @ToolParam(description = "Issue description") String issueDescription,

                        @ToolParam(description = "Namespace") String namespace) {

                log.info(
                                "MCP Tool: retrieve_recent_investigations called for issue [{}] in namespace [{}]",
                                issueDescription,
                                namespace);

                eventService.publish(
                                investigationId,
                                EventType.AGENT_STARTED,
                                "CONTEXT_AGENT",
                                "Searching recent investigations",
                                "");

                try {

                        LocalDateTime cutoff = LocalDateTime.now().minusHours(1);
                        log.debug("Searching for completed investigations in namespace '{}' since '{}'", namespace,
                                        cutoff);

                        List<Investigation> investigations = investigationRepository
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

                        if (result.isBlank()) {
                                result = """
                                                No similar investigations found
                                                in the last hour.
                                                """;
                        }

                        eventService.publish(
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
                        eventService.publish(
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