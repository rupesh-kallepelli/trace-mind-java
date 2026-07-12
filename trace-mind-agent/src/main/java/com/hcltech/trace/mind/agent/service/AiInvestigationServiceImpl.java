package com.hcltech.trace.mind.agent.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.hcltech.trace.mind.agent.entities.EventType;
import com.hcltech.trace.mind.agent.entities.InvestigationEvidence;
import com.hcltech.trace.mind.agent.request.CreateInvestigationRequest;
import com.hcltech.trace.mind.agent.response.InvestigationResponse;
import com.hcltech.trace.mind.agent.tools.ApplicationInvestigationAgent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiInvestigationServiceImpl
                implements AiInvestigationService {

        private final ChatClient chatClient;
        private final ApplicationInvestigationAgent applicationInvestigationAgent;
        private final InvestigationEventService eventService;
        private final InvestigationService investigationService;
        private final InvestigationEvidenceService evidenceService;
        @Value("${namespace}")
        private String namespace;
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

        @Async
        public void executeInvestigation(
                        String investigationId,
                        String issue) {

                try {

                        investigationService.markRunning(
                                        investigationId);

                        eventService.publish(
                                        investigationId,
                                        EventType.INVESTIGATION_STARTED,
                                        "ORCHESTRATOR",
                                        "Investigation started",
                                        null);

                        String report = chatClient.prompt()
                                        .system(SYSTEM_PROMPT)
                                        .user("""
                                                        Analyze the following production issue.

                                                        investigationId: %s

                                                        Issue:
                                                        %s

                                                        Namespace:
                                                        %s
                                                        """
                                                        .formatted(
                                                                        investigationId,
                                                                        issue,
                                                                        namespace))
                                        .tools(applicationInvestigationAgent)
                                        .call()
                                        .content();

                        evidenceService.save(
                                        InvestigationEvidence.builder()
                                                        .investigationId(investigationId)
                                                        .agentType("MAIN_AGENT")
                                                        .summary(
                                                                        "RCA Investigation Completed")
                                                        .evidence(
                                                                        Map.of(
                                                                                        "issue",
                                                                                        issue,
                                                                                        "namespace",
                                                                                        namespace,
                                                                                        "report",
                                                                                        report))
                                                        .createdAt(
                                                                        LocalDateTime.now())
                                                        .build());

                        investigationService.updateResult(
                                        investigationId,
                                        report,
                                        90.0,
                                        report);

                        investigationService.markCompleted(
                                        investigationId);

                        eventService.publish(
                                        investigationId,
                                        EventType.INVESTIGATION_COMPLETED,
                                        "ORCHESTRATOR",
                                        "Investigation completed",
                                        null);

                } catch (Exception ex) {

                        investigationService.markFailed(
                                        investigationId);

                        eventService.publish(
                                        investigationId,
                                        EventType.INVESTIGATION_FAILED,
                                        "ORCHESTRATOR",
                                        ex.getMessage(),
                                        null);

                        throw new RuntimeException(ex);
                }
        }

        @Override
        public InvestigationResponse createInvestigation(String issue, String email) {

                return investigationService.create(
                                CreateInvestigationRequest.builder()
                                                .issueDescription(issue)
                                                .serviceName("UNKNOWN")
                                                .namespace(namespace)
                                                .createdBy(email)
                                                .build());
        }

}