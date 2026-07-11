package com.hcltech.trace.mind.agent.tools;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.hcltech.trace.mind.agent.entities.EventType;
import com.hcltech.trace.mind.agent.service.InvestigationEventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationInvestigationAgent {

        private final ChatClient chatClient;
        private final ToolCallbackProvider toolCallbackProvider;
        private final InvestigationEventService eventService;
        @Value("${agent.main.tool-names}")
        private Set<String> toolNames;

        @Value("classpath:/prompts/main-agent.md")
        private Resource mainAgentResource;
        @Value("classpath:/prompts/app-context.md")
        private Resource appContext;

        @Tool(name = "investigate_application_issue", description = """
                        Perform complete root cause analysis.

                        MANDATORY:
                        - investigationId must be passed to all downstream agents and tools.
                        - investigationId is used for event correlation and streaming.

                        Uses:
                        - Log Investigation Agent
                        - Database Investigation Agent
                        - Source Code Investigation Agent
                        - Runtime Investigation Agent

                        Produces a consolidated RCA report.
                        """)
        public String investigateApplicationIssue(

                        @ToolParam(description = "Unique investigation identifier") String investigationId,

                        @ToolParam(description = "Issue description") String issueDescription,

                        @ToolParam(description = "Application namespace") String namespace) throws Exception {

                log.info("MCP Tool: investigateApplicationIssue called for issue [{}] namespace [{}]", issueDescription,
                                namespace);
                log.info("""
                                Main Agent Input
                                investigationId={}
                                issueDescription={}
                                namespace={}
                                """,
                                investigationId,
                                issueDescription,
                                namespace);
                log.info("filter for {}", toolNames);

                try {
                        // BeanOutputConverter<ApplicationInvestigationResponse> converter = new
                        // BeanOutputConverter<>(
                        // ApplicationInvestigationResponse.class);

                        ToolCallback[] investigationAgents = Arrays.stream(
                                        toolCallbackProvider.getToolCallbacks())
                                        .filter(tool -> {

                                                String toolName = tool.getToolDefinition()
                                                                .name();

                                                return toolNames.contains(toolName);

                                        })
                                        .toArray(ToolCallback[]::new);

                        log.info("Main RCA Agent discovered {} investigation agents", investigationAgents.length);

                        Arrays.stream(investigationAgents)
                                        .forEach(tool -> log.debug("Available Investigation Agent : {}",
                                                        tool.getToolDefinition().name()));

                        String prompt = appContext.getContentAsString(StandardCharsets.UTF_8) +
                                        "\n" + mainAgentResource.getContentAsString(StandardCharsets.UTF_8);
                        eventService.publish(
                                        investigationId,
                                        EventType.AGENT_STARTED,
                                        "ORCHESTRATOR",
                                        "Investigation started",
                                        null);
                        String result = chatClient.prompt()
                                        .system(prompt
                                        // + "\n\n" + converter.getFormat()
                                        )
                                        .user("""
                                                        Perform a complete RCA investigation.

                                                        investigationId: %s

                                                        Issue:
                                                        %s

                                                        Namespace:
                                                        %s

                                                        Use all available investigation agents.

                                                        Correlate findings from logs,
                                                        runtime, database and source code.

                                                        Determine the most probable root cause.

                                                        Generate recommendations and preventive actions.
                                                        """
                                                        .formatted(investigationId,
                                                                        issueDescription,
                                                                        namespace))
                                        .toolCallbacks(investigationAgents)
                                        .call()
                                        .content();

                        // ApplicationInvestigationResponse response = converter.convert(result);

                        // if (response != null) {
                        // response.setIssue(issueDescription);
                        // response.setNamespace(namespace);
                        // }
                        eventService.publish(
                                        investigationId,
                                        EventType.AGENT_COMPLETED,
                                        "ORCHESTRATOR",
                                        "Investigation completed",
                                        result);
                        log.info("Completed RCA investigation for issue [{}], response : [{}]", issueDescription,
                                        result);

                        return result;
                } catch (Exception e) {
                        eventService.publish(
                                        investigationId,
                                        EventType.AGENT_FAILED,
                                        "ORCHESTRATOR",
                                        "Investigation completed",
                                        e.getMessage());
                        log.error("MCP Tool: investigateApplicationIssue failed for {}: {}", issueDescription,
                                        e.getMessage(), e);
                        throw e;
                }
        }
}