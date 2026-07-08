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
public class DatabaseAgentTools {

        private final ChatClient chatClient;
        private final ToolCallbackProvider toolCallbackProvider;
        private final TraceMindEventClient eventClient;

        @Value("${agent.database.tool-prefix:database_}")
        private String toolPrefix;

        @Value("classpath:/prompts/database-agent.md")
        private Resource databaseAgentResource;

        @Value("classpath:/prompts/database-schema.md")
        private Resource databaseSchema;

        @Value("classpath:/prompts/app-context.md")
        private Resource appContext;

        @McpTool(name = "investigate_database", description = """
                        Analyze database related evidence for a given investigation.

                        Mandatory Inputs:
                        - investigationId: unique investigation identifier used for event correlation.
                        - issueDescription: issue being investigated.

                        Identify:
                        - affected tables
                        - FK violations
                        - constraint violations
                        - duplicate records
                        - missing indexes
                        - schema related issues

                        Return investigation findings.
                        """)
        public String investigateDatabase(
                        @McpToolParam(description = "investigationId") String investigationId,
                        @McpToolParam(description = "Application issue description") String issueDescription)
                        throws Exception {
                log.info("""
                                Database Agent Input

                                investigationId={}
                                issueDescription={}
                                """,
                                investigationId,
                                issueDescription);
                log.info("MCP Tool: investigateDatabase called for: {}", issueDescription);
                try {
                        // BeanOutputConverter<DatabaseInvestigationResponse> converter = new
                        // BeanOutputConverter<>(
                        // DatabaseInvestigationResponse.class);

                        ToolCallback[] databaseTools = Arrays.stream(toolCallbackProvider.getToolCallbacks())
                                        .filter(tool -> {
                                                String name = tool.getToolDefinition().name();
                                                return name.startsWith(toolPrefix);
                                        }).toArray(ToolCallback[]::new);

                        log.debug("MCP Tool: investigateDatabase using {} database tools", databaseTools.length);

                        Arrays.stream(databaseTools).forEach(
                                        tool -> log.debug("Database Agent Tool: {}", tool.getToolDefinition().name()));

                        String prompt = appContext.getContentAsString(StandardCharsets.UTF_8) +
                                        "\n" + databaseSchema.getContentAsString(StandardCharsets.UTF_8) +
                                        "\n" + databaseAgentResource.getContentAsString(StandardCharsets.UTF_8);
                        eventClient.publishEvent(
                                        investigationId,
                                        EventType.AGENT_STARTED,
                                        "DB_AGENT",
                                        "Searching DB",
                                        null);

                        String result = chatClient.prompt()
                                        .system(prompt
                                        // + "\n\n" + converter.getFormat()
                                        )
                                        .user("Investigate database related evidence for issue: " + issueDescription)
                                        .toolCallbacks(databaseTools)
                                        .call()
                                        .content();
                        eventClient.publishEvent(
                                        investigationId,
                                        EventType.AGENT_COMPLETED,
                                        "DB_AGENT",
                                        "DB analysis completed",
                                        result);
                        // DatabaseInvestigationResponse response = converter.convert(result);
                        log.debug("MCP Tool: investigateDatabase result: {}",
                                        result);
                        return result;
                } catch (Exception e) {
                        eventClient.publishEvent(
                                        investigationId,
                                        EventType.AGENT_FAILED,
                                        "DB_AGENT",
                                        "DB analysis completed",
                                        e.getMessage());
                        log.error("MCP Tool: investigateDatabase failed for {}: {}", issueDescription, e.getMessage(),
                                        e);
                        throw e;
                }
        }
}