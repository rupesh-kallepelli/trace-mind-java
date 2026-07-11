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

import com.hcltech.orchestrator_agent.request.EventType;
import com.hcltech.orchestrator_agent.service.TraceMindEventClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class TraceAgentTools {

        private final ChatClient chatClient;
        private final ToolCallbackProvider toolCallbackProvider;
        private final TraceMindEventClient eventClient;
        @Value("${agent.traces.tool-prefixes:trace_}")
        private Set<String> toolPrefixes;

        @Value("classpath:/prompts/traces-agent.md")
        private Resource traceAgentResource;

        @Value("classpath:/prompts/app-context.md")
        private Resource appContext;

        @McpTool(name = "investigate_traces", description = """
                        Investigate distributed traces for a specific investigation.

                        Mandatory Inputs:
                        - investigationId: Unique investigation identifier used for event correlation.
                        - issueDescription: Description of the issue being investigated.
                        - namespace: Kubernetes namespace containing the affected application.

                        Analyze:
                        - slow requests
                        - bottlenecks
                        - latency contributors
                        - failed spans
                        - dependency failures
                        - error propagation

                        The investigationId must be preserved and used when publishing
                        investigation events.

                        Return trace investigation findings and supporting evidence.
                        """)
        public String investigateTraces(

                        @McpToolParam(description = """
                                        Unique investigation identifier received from the orchestrator.
                                        Must be propagated unchanged for event tracking and streaming.
                                        """) String investigationId,

                        @McpToolParam(description = """
                                        Description of the application issue being investigated.
                                        """) String issueDescription,

                        @McpToolParam(description = """
                                        Kubernetes namespace containing the affected application.
                                        """) String namespace)

                        throws Exception {

                log.info(
                                "MCP Tool: investigateTraces called for issue [{}] in namespace [{}]",
                                issueDescription,
                                namespace);

                try {
                        ToolCallback[] traceTools = Arrays.stream(
                                        toolCallbackProvider.getToolCallbacks())
                                        .filter(tool -> {
                                                String toolName = tool.getToolDefinition()
                                                                .name()
                                                                .toLowerCase();

                                                return toolPrefixes.stream()
                                                                .anyMatch(toolName::startsWith);
                                        })
                                        .toArray(ToolCallback[]::new);

                        log.info(
                                        "Trace Agent discovered {} tracing tools",
                                        traceTools.length);

                        Arrays.stream(traceTools)
                                        .forEach(tool -> log.debug(
                                                        "Trace Agent Tool: {}",
                                                        tool.getToolDefinition().name()));

                        String prompt = appContext.getContentAsString(StandardCharsets.UTF_8)
                                        + "\n"
                                        + traceAgentResource.getContentAsString(
                                                        StandardCharsets.UTF_8);
                        eventClient.publishEvent(
                                        investigationId,
                                        EventType.AGENT_STARTED,
                                        "TRACE_AGENT",
                                        "Trace analysis started",
                                        null);
                        String result = chatClient.prompt()
                                        .system(prompt)
                                        .user("""
                                                        Investigate the following issue.

                                                        Issue:
                                                        %s

                                                        Namespace:
                                                        %s

                                                        Use tracing tools to identify:

                                                        - bottlenecks
                                                        - dependency failures
                                                        - slow spans
                                                        - failed spans
                                                        - error propagation
                                                        - critical path

                                                        Determine the most probable
                                                        trace-based root cause.
                                                        """
                                                        .formatted(
                                                                        issueDescription,
                                                                        namespace))
                                        .toolCallbacks(traceTools)
                                        .call()
                                        .content();
                        eventClient.publishEvent(
                                        investigationId,
                                        EventType.AGENT_COMPLETED,
                                        "TRACE_AGENT",
                                        "Trace analysis complete",
                                        result);
                        log.debug(
                                        "MCP Tool: investigateTraces result: {}",
                                        result);

                        return result;
                } catch (Exception e) {
                        eventClient.publishEvent(
                                        investigationId,
                                        EventType.AGENT_FAILED,
                                        "TRACE_AGENT",
                                        "Trace analysis failed",
                                        e.getMessage());
                        log.error(
                                        "MCP Tool: investigateTraces failed for issue [{}], namespace [{}]: {}",
                                        issueDescription, namespace, e.getMessage(), e);
                        throw e;
                }
        }
}
