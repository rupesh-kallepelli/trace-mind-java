package com.hcltech.orchestrator_agent.agents;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.hcltech.orchestrator_agent.response.SourceCodeInvestigationResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SourceCodeAgentTools {

        private final ChatClient chatClient;
        private final ToolCallbackProvider toolCallbackProvider;

        @Value("${agent.sourcecode.tools:search_repositories,search_code,search_issues,search_pull_requests,get_file_contents,get_commit,list_commits,list_branches}")
        private Set<String> githubToolNames;

        @Value("classpath:/prompts/source-code-agent.md")
        private Resource sourceCodeAgentResource;
        @Value("classpath:/prompts/app-context.md")
        private Resource appContext;

        // @McpTool(name = "investigate_source_code", description = """
        // Investigate source code related evidence.

        // Analyze:
        // - repositories
        // - classes
        // - methods
        // - commits
        // - pull requests
        // - recent code changes

        // Return investigation findings.
        // """)
        public SourceCodeInvestigationResponse investigateSourceCode(

                        @McpToolParam(description = "Application issue description") String issueDescription)
                        throws Exception {
                log.info("MCP Tool: investigateSourceCode called for: {}", issueDescription);
                try {
                        BeanOutputConverter<SourceCodeInvestigationResponse> converter = new BeanOutputConverter<>(
                                        SourceCodeInvestigationResponse.class);

                        ToolCallback[] githubTools = Arrays.stream(toolCallbackProvider.getToolCallbacks())
                                        .filter(tool -> githubToolNames.contains(
                                                        tool.getToolDefinition().name()))
                                        .toArray(ToolCallback[]::new);

                        log.debug("MCP Tool: investigateSourceCode using {} github tools", githubTools.length);

                        Arrays.stream(githubTools)
                                        .forEach(tool -> log.debug(
                                                        "Source Code Agent Tool: {}",
                                                        tool.getToolDefinition().name()));

                        String prompt = appContext.getContentAsString(StandardCharsets.UTF_8) +
                                        "\n" + sourceCodeAgentResource.getContentAsString(StandardCharsets.UTF_8);

                        String result = chatClient.prompt()
                                        .system(prompt + "\n\n" + converter.getFormat())
                                        .user("Investigate source code related evidence for issue: " + issueDescription)
                                        .toolCallbacks(githubTools)
                                        .call()
                                        .content();

                        SourceCodeInvestigationResponse response = converter.convert(result);
                        log.debug("MCP Tool: investigateSourceCode result: {}", response);
                        return response;
                } catch (Exception e) {
                        log.error("MCP Tool: investigateSourceCode failed for {}: {}", issueDescription, e.getMessage(),
                                        e);
                        throw e;
                }
        }
}