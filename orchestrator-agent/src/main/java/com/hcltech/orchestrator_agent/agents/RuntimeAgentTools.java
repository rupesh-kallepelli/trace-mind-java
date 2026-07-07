package com.hcltech.orchestrator_agent.agents;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import com.hcltech.orchestrator_agent.response.RuntimeInvestigationResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RuntimeAgentTools {

        private final ObjectMapper objectMapper;
        private final ChatClient chatClient;
        private final ToolCallbackProvider toolCallbackProvider;

        @Value("${agent.runtime.tool-prefixes:pods_,deployment,deployments,events,node,nodes,service,configmap,ingress}")
        private Set<String> toolPrefixes;

        @Value("classpath:/prompts/runtime-agent.md")
        private Resource runtimeAgentResource;
        @Value("classpath:/prompts/app-context.md")
        private Resource appContext;

        @McpTool(name = "investigate_runtime", description = """
                        Investigate Kubernetes runtime health.

                        Analyze:
                        - deployments
                        - pods
                        - restarts
                        - cluster events
                        - resource issues
                        - runtime failures
                        - scheduling failures
                        - namespace health

                        Return investigation findings.
                        """)
        public RuntimeInvestigationResponse investigateRuntime(
                        @McpToolParam(description = "Application issue description") String issueDescription,
                        @McpToolParam(description = "Kubernetes namespace") String namespace) throws Exception {

                log.info("MCP Tool: investigateRuntime called for issue [{}] in namespace [{}]", namespace);

                try {

                        BeanOutputConverter<RuntimeInvestigationResponse> converter = new BeanOutputConverter<>(
                                        RuntimeInvestigationResponse.class);

                        ToolCallback[] kubernetesTools = Arrays.stream(toolCallbackProvider.getToolCallbacks())
                                        .filter(tool -> {
                                                String toolName = tool.getToolDefinition().name().toLowerCase();
                                                return toolPrefixes.stream().anyMatch(toolName::startsWith);
                                        }).toArray(ToolCallback[]::new);

                        log.debug("Runtime Agent discovered {} Kubernetes tools", kubernetesTools.length);

                        Arrays.stream(kubernetesTools)
                                        .forEach(tool -> log.debug("Runtime Agent Tool: {}",
                                                        tool.getToolDefinition().name()));

                        String prompt = appContext.getContentAsString(StandardCharsets.UTF_8) + "\n"
                                        + runtimeAgentResource.getContentAsString(StandardCharsets.UTF_8);

                        String result = chatClient.prompt()
                                        .system(prompt + "\n\n" + converter.getFormat())
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

                        RuntimeInvestigationResponse response = converter.convert(result);

                        if (response != null) {
                                response.setNamespace(namespace);
                        }

                        log.debug("MCP Tool: investigateRuntime result: {}",
                                        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));

                        return response;

                } catch (Exception e) {

                        log.error("MCP Tool: investigateRuntime failed for issue [{}], namespace [{}]: {}",
                                        issueDescription, namespace, e.getMessage(), e);

                        throw e;
                }
        }
}