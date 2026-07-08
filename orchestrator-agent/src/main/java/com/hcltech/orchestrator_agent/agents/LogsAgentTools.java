package com.hcltech.orchestrator_agent.agents;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.hcltech.orchestrator_agent.request.EventType;
import com.hcltech.orchestrator_agent.service.TraceMindEventClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class LogsAgentTools {

        private final ChatClient chatClient;
        private final ToolCallbackProvider toolCallbackProvider;
        private final TraceMindEventClient eventClient;

        @Value("${agent.logs.tool-prefix:opensearch_}")
        private String toolPrefix;

        @Value("classpath:/prompts/logs-agent.md")
        private Resource logAgentResource;
        @Value("classpath:/prompts/app-context.md")
        private Resource appContext;

        @McpTool(name = "investigate_logs", description = """
                        Investigate application logs for a specific investigation.

                        Mandatory Inputs:
                        - investigationId: Unique investigation identifier used for event correlation.
                        - issueDescription: Description of the application issue.

                        Responsibilities:
                        - Search application logs
                        - Identify errors and exceptions
                        - Detect patterns related to the issue
                        - Gather evidence for root cause analysis

                        Returns investigation findings and supporting log evidence.
                        """)
        public String investigateLogs(
                        @McpToolParam(description = "investigationId") String investigationId,
                        @McpToolParam(description = "Issue") String issueDescription)
                        throws Exception {
                log.info("""
                                Logs Agent Input

                                investigationId={}
                                issueDescription={}
                                """,
                                investigationId,
                                issueDescription);
                log.info("MCP Tool: investigateLogs called for: {}", issueDescription);
                try {
                        // BeanOutputConverter<LogInvestigationResponse> converter = new
                        // BeanOutputConverter<>(
                        // LogInvestigationResponse.class);

                        ToolCallback[] logTools = Arrays.stream(toolCallbackProvider.getToolCallbacks())
                                        .filter(tool -> {
                                                String name = tool.getToolDefinition().name();
                                                return name.startsWith(toolPrefix);
                                        })
                                        .toArray(ToolCallback[]::new);

                        log.debug("MCP Tool: investigateLogs using {} log tools", logTools.length);

                        Arrays.stream(logTools)
                                        .forEach(tool -> log.debug(
                                                        "Logs Agent Tool: {}",
                                                        tool.getToolDefinition().name()));

                        String prompt = appContext.getContentAsString(StandardCharsets.UTF_8) +
                                        "\n" + logAgentResource.getContentAsString(StandardCharsets.UTF_8);
                        eventClient.publishEvent(
                                        investigationId,
                                        EventType.AGENT_STARTED,
                                        "LOGS_AGENT",
                                        "Searching logs",
                                        null);
                        String result = chatClient.prompt()
                                        .system(prompt
                                        // + "\n\n" + converter.getFormat()
                                        )
                                        .user("Investigate issue: " + issueDescription)
                                        .toolCallbacks(logTools)
                                        .call()
                                        .content();
                        eventClient.publishEvent(
                                        investigationId,
                                        EventType.AGENT_COMPLETED,
                                        "LOGS_AGENT",
                                        "Logs analysis completed",
                                        result);
                        // LogInvestigationResponse response = converter.convert(result);
                        log.debug("MCP Tool: investigateLogs result: {}",
                                        result);
                        return result;
                } catch (Exception e) {
                        eventClient.publishEvent(
                                        investigationId,
                                        EventType.AGENT_FAILED,
                                        "LOGS_AGENT",
                                        "Logs analysis failed",
                                        e.getMessage());
                        log.error("MCP Tool: investigateLogs failed for {}: {}", issueDescription, e.getMessage(), e);
                        throw e;
                }
        }
}