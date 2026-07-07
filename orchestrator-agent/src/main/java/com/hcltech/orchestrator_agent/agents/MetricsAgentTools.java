package com.hcltech.orchestrator_agent.agents;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class MetricsAgentTools {

    private final ChatClient chatClient;
    private final ToolCallbackProvider toolCallbackProvider;

    @Value("${agent.metrics.tool-prefixes:metric_}")
    private Set<String> toolPrefixes;

    @Value("classpath:/prompts/metrics-agent.md")
    private Resource metricsAgentResource;

    @Value("classpath:/prompts/app-context.md")
    private Resource appContext;

    @McpTool(
            name = "investigate_metrics",
            description = """
                    Investigate application and infrastructure metrics.

                    Analyze:

                    - latency
                    - error rates
                    - throughput
                    - request volume
                    - cpu utilization
                    - memory utilization
                    - restart trends
                    - traffic patterns
                    - service degradation

                    Return metrics investigation findings.
                    """
    )
    public String investigateMetrics(
            @McpToolParam(description = "Application issue description")
            String issueDescription,

            @McpToolParam(description = "Kubernetes namespace")
            String namespace) throws Exception {

        log.info(
                "MCP Tool: investigateMetrics called for issue [{}] in namespace [{}]",
                issueDescription,
                namespace);

        try {

            ToolCallback[] metricsTools = Arrays.stream(
                            toolCallbackProvider.getToolCallbacks())
                    .filter(tool -> {
                        String toolName =
                                tool.getToolDefinition()
                                        .name()
                                        .toLowerCase();

                        return toolPrefixes.stream()
                                .anyMatch(toolName::startsWith);
                    })
                    .toArray(ToolCallback[]::new);

            log.info(
                    "Metrics Agent discovered {} metrics tools",
                    metricsTools.length);

            Arrays.stream(metricsTools)
                    .forEach(tool ->
                            log.debug(
                                    "Metrics Agent Tool: {}",
                                    tool.getToolDefinition().name()));

            String prompt =
                    appContext.getContentAsString(StandardCharsets.UTF_8)
                            + "\n"
                            + metricsAgentResource.getContentAsString(
                                    StandardCharsets.UTF_8);

            String result = chatClient.prompt()
                    .system(prompt)
                    .user("""
                            Investigate the following application issue.

                            Issue:
                            %s

                            Namespace:
                            %s

                            Restrict all investigations to this namespace.

                            Use Metrics tools to:

                            - Identify latency issues
                            - Analyze P50, P95 and P99 latency
                            - Analyze error rate trends
                            - Analyze request volume
                            - Analyze CPU utilization
                            - Analyze memory utilization
                            - Analyze container restart trends
                            - Analyze traffic spikes
                            - Identify service degradation

                            Collect metric evidence.

                            Determine whether the issue is caused by:

                            - latency degradation
                            - resource saturation
                            - traffic spikes
                            - error rate increase
                            - availability degradation
                            - service performance issues

                            Determine the most probable metrics-based root cause.
                            """.formatted(
                            issueDescription,
                            namespace))
                    .toolCallbacks(metricsTools)
                    .call()
                    .content();

            log.debug(
                    "MCP Tool: investigateMetrics result: {}",
                    result);

            return result;

        } catch (Exception e) {

            log.error(
                    "MCP Tool: investigateMetrics failed for issue [{}], namespace [{}]: {}",
                    issueDescription,
                    namespace,
                    e.getMessage(),
                    e);

            throw e;
        }
    }
}
