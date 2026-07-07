package com.hcltech.trace.mind.agent.controller;

import org.springframework.ai.chat.client.ChatClient;
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

        private final ApplicationInvestigationAgent applicationInvestigationAgent;
        private final ChatClient chatClient;

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
                        @RequestParam String namespace) throws Exception {
                try {
                        // BeanOutputConverter<ApplicationInvestigationResponse> converter = new
                        // BeanOutputConverter<>(
                        // ApplicationInvestigationResponse.class);
                        String clientResponse = chatClient.prompt()
                                        .system(SYSTEM_PROMPT
                                        // + "\n\n" + converter.getFormat()
                                        )
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

                        // ApplicationInvestigationResponse response =
                        // converter.convert(clientResponse);

                        // if (response != null) {
                        // response.setIssue(issue);
                        // response.setNamespace(namespace);
                        // }

                        log.info("Completed RCA investigation for issue [{}], response : [{}]", issue, clientResponse);
                        return ResponseEntity.ok(clientResponse);
                } catch (Exception e) {
                        log.error("MCP Tool: investigateApplicationIssue failed for {}: {}", issue, e.getMessage(), e);
                        throw e;
                }

        }

}