package com.hcltech.orchestrator_agent.agents;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.hcltech.orchestrator_agent.response.DatabaseInvestigationResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatabaseAgentTools {

    
        private final ChatClient chatClient;
        private final ToolCallbackProvider toolCallbackProvider;

        @Value("${agent.database.tool-prefix:database_}")
        private String toolPrefix;

        @Value("classpath:/prompts/database-agent.md")
        private Resource databaseAgentResource;

        @Value("classpath:/prompts/database-schema.md")
        private Resource databaseSchema;

        @Value("classpath:/prompts/app-context.md")
        private Resource appContext;

        @McpTool(name = "investigate_database", description = """
                        Analyze database related evidence.

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
                        @McpToolParam(description = "Application issue description") String issueDescription)
                        throws Exception {

                log.info("MCP Tool: investigateDatabase called for: {}", issueDescription);
                try {
                        BeanOutputConverter<DatabaseInvestigationResponse> converter = new BeanOutputConverter<>(
                                        DatabaseInvestigationResponse.class);

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

                        String result = chatClient.prompt()
                                        .system(prompt + "\n\n" + converter.getFormat())
                                        .user("Investigate database related evidence for issue: " + issueDescription)
                                        .toolCallbacks(databaseTools)
                                        .call()
                                        .content();

                        // DatabaseInvestigationResponse response = converter.convert(result);
                        log.debug("MCP Tool: investigateDatabase result: {}",
                                        result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: investigateDatabase failed for {}: {}", issueDescription, e.getMessage(),
                                        e);
                        throw e;
                }
        }
}