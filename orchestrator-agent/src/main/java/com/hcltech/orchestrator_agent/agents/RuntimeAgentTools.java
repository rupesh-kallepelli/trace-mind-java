package com.hcltech.orchestrator_agent.agents;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;
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
public class RuntimeAgentTools {

        private final ChatClient chatClient;
        private final ToolCallbackProvider toolCallbackProvider;
        private final TraceMindEventClient eventClient;
        @Value("${agent.runtime.tool-prefixes:pods_,deployment,deployments,events,node,nodes,service,configmap,ingress}")
        private Set<String> toolPrefixes;

        @Value("classpath:/prompts/runtime-agent.md")
        private Resource runtimeAgentResource;
        @Value("classpath:/prompts/app-context.md")
        private Resource appContext;

        @McpTool(name = "investigate_runtime", description = """
                        Investigate Kubernetes runtime health for a specific investigation.

                        Mandatory Inputs:
                        - investigationId: Unique investigation identifier used for event correlation.
                        - issueDescription: Description of the issue being investigated.
                        - namespace: Kubernetes namespace containing the affected application.

                        Analyze:
                        - deployments
                        - pods
                        - restarts
                        - cluster events
                        - resource issues
                        - runtime failures
                        - scheduling failures
                        - namespace health

                        The investigationId must be preserved and used when publishing
                        investigation events.

                        Return investigation findings and supporting evidence.
                        """)
        public String investigateRuntime(

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

                log.info("MCP Tool: investigateRuntime called for issue [{}] in namespace [{}]", namespace);
                log.info("""
                                Runtime Agent Input

                                investigationId={}
                                issueDescription={}
                                namespace={}
                                """,
                                investigationId,
                                issueDescription,
                                namespace);
                try {

                        // BeanOutputConverter<RuntimeInvestigationResponse> converter = new
                        // BeanOutputConverter<>(
                        // RuntimeInvestigationResponse.class);

                        ToolCallback[] kubernetesTools = Arrays.stream(toolCallbackProvider.getToolCallbacks())
                                        .filter(tool -> {
                                                String toolName = tool.getToolDefinition().name().toLowerCase();
                                                return toolPrefixes.stream().anyMatch(toolName::startsWith);
                                        })
                                        .filter(tool -> !tool.getToolDefinition().name().toLowerCase()
                                                        .equals("pods_exec"))
                                        .toArray(ToolCallback[]::new);

                        log.debug("Runtime Agent discovered {} Kubernetes tools", kubernetesTools.length);

                        Arrays.stream(kubernetesTools)
                                        .forEach(tool -> log.debug("Runtime Agent Tool: {}",
                                                        tool.getToolDefinition().name()));

                        String prompt = appContext.getContentAsString(StandardCharsets.UTF_8) + "\n"
                                        + runtimeAgentResource.getContentAsString(StandardCharsets.UTF_8);
                        eventClient.publishEvent(
                                        investigationId,
                                        EventType.AGENT_STARTED,
                                        "RUNTIME_AGENT",
                                        "Runtime analysis started",
                                        null);
                        String result = chatClient.prompt()
                                        .system(prompt
                                        // + "\n\n" + converter.getFormat()
                                        )
                                        .user("""
                                                        Investigate the following runtime issue.

                                                        Issue:
                                                        %s

                                                        Namespace:
                                                        %s

                                                        Restrict all investigations to this namespace.

                                                        Use Kubernetes tools to:

                                                        - Identify affected deployments
                                                        - Identify unhealthy pods
                                                        - Analyze restart counts
                                                        - Analyze cluster events
                                                        - Analyze service health
                                                        - Analyze resource issues

                                                        Collect evidence and determine the most probable runtime root cause.
                                                        """
                                                        .formatted(issueDescription, namespace))
                                        .toolCallbacks(kubernetesTools)
                                        .call()
                                        .content();

                        // RuntimeInvestigationResponse response = converter.convert(result);

                        // if (response != null) {
                        // response.setNamespace(namespace);
                        // }
                        eventClient.publishEvent(
                                        investigationId,
                                        EventType.AGENT_COMPLETED,
                                        "RUNTIME_AGENT",
                                        "Runtime analysis complete",
                                        result);
                        log.debug("MCP Tool: investigateRuntime result: {}",
                                        result);

                        return result;

                } catch (Exception e) {
                        eventClient.publishEvent(
                                        investigationId,
                                        EventType.AGENT_FAILED,
                                        "RUNTIME_AGENT",
                                        "Runtime analysis failed",
                                        e.getMessage());
                        log.error("MCP Tool: investigateRuntime failed for issue [{}], namespace [{}]: {}",
                                        issueDescription, namespace, e.getMessage(), e);

                        throw e;
                }
        }
}