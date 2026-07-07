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

import com.hcltech.orchestrator_agent.response.LogInvestigationResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class LogsAgentTools {

        private final ChatClient chatClient;
        private final ToolCallbackProvider toolCallbackProvider;

        @Value("${agent.logs.tool-prefix:opensearch_}")
        private String toolPrefix;

        @Value("classpath:/prompts/logs-agent.md")
        private Resource logAgentResource;
        @Value("classpath:/prompts/app-context.md")
        private Resource appContext;

        @McpTool(name = "investigate_logs", description = "Investigate application logs for issues")
        public String investigateLogs(
                        @McpToolParam(description = "Issue") String issueDescription)
                        throws Exception {

                log.info("MCP Tool: investigateLogs called for: {}", issueDescription);
                try {
                        BeanOutputConverter<LogInvestigationResponse> converter = new BeanOutputConverter<>(
                                        LogInvestigationResponse.class);

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

                        String result = chatClient.prompt()
                                        .system(prompt + "\n\n" + converter.getFormat())
                                        .user("Investigate issue: " + issueDescription)
                                        .toolCallbacks(logTools)
                                        .call()
                                        .content();

                        // LogInvestigationResponse response = converter.convert(result);
                        log.debug("MCP Tool: investigateLogs result: {}",
                                        result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: investigateLogs failed for {}: {}", issueDescription, e.getMessage(), e);
                        throw e;
                }
        }
}