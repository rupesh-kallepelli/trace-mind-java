package com.hcltech.trace.mind.agent.controller;

import java.util.Arrays;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcltech.trace.mind.agent.tools.ApplicationInvestigationAgent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/observai")
@RequiredArgsConstructor
@Slf4j
public class ObservaAIController {

        private final ChatClient chatClient;
        // private final ToolCallbackProvider toolCallbackProvider;
        private final ApplicationInvestigationAgent applicationInvestigationAgent;
        private static final String SYSTEM_PROMPT = """
                        You are ObservaAI.

                        You are an expert SRE, Platform Engineer,
                        Production Support Engineer and RCA Investigator.

                        You have access to a dedicated RCA tool:

                        investigate_application_issue

                        RULES

                        1. Always use investigate_application_issue.
                        2. Never guess.
                        3. Never hallucinate.
                        4. Never invent evidence.
                        5. Base all findings on investigation results.
                        6. Produce a clear RCA summary.
                        """;

        @GetMapping("/analyze")
        public ResponseEntity<String> analyze(
                        @RequestParam String issue,
                        @RequestParam String namespace) {

                // ToolCallback[] tools = Arrays.stream(toolCallbackProvider.getToolCallbacks())
                // .filter(tool -> {

                // String name = tool.getToolDefinition().name();

                // return name!= null && name.startsWith("investigate_");
                // })
                // .toArray(ToolCallback[]::new);
                // log.info("===== TOOLS PASSED TO MODEL =====");

                // Arrays.stream(tools)
                // .forEach(tool -> log.info(
                // "Tool: {}",
                // tool.getToolDefinition().name()));

                String response = chatClient.prompt()
                                .system(SYSTEM_PROMPT)
                                .user("""
                                                Analyze the following production issue.

                                                Issue:
                                                %s

                                                Namespace:
                                                %s
                                                """
                                                .formatted(issue, namespace))
                                .tools(applicationInvestigationAgent)
                                .call()
                                .content();

                return ResponseEntity.ok(response);
        }
}
