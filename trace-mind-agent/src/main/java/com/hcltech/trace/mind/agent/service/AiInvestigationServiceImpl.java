package com.hcltech.trace.mind.agent.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hcltech.trace.mind.agent.entities.EventType;
import com.hcltech.trace.mind.agent.entities.Incident;
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
        private final IncidentService incidentService;
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

        @Override
        public String investigate(String issue) throws Exception {
                log.info("Starting AI investigation for issue: '{}' in namespace: {}", issue, namespace);
                InvestigationResponse investigation = investigationService.create(
                                CreateInvestigationRequest.builder()
                                                .issueDescription(issue)
                                                .serviceName("UNKNOWN")
                                                .namespace(namespace)
                                                .build());

                String investigationId = investigation.getId().toString();
                log.debug("Created initial investigation record with ID: {}", investigationId);

                try {
                        investigationService.markRunning(investigationId);
                        log.debug("Sending prompt to ChatClient for investigation: {}", investigationId);
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
                                                        .formatted(investigationId, issue, namespace))
                                        .tools(applicationInvestigationAgent)
                                        .call()
                                        .content();
                        eventService.publish(
                                        investigationId,
                                        EventType.INVESTIGATION_STARTED,
                                        "ORCHESTRATOR",
                                        "Investigation started",
                                        "{}");
                        log.debug("AI analysis completed. Report size: {} characters",
                                        report != null ? report.length() : 0);

                        evidenceService.save(
                                        InvestigationEvidence.builder()
                                                        .investigationId(
                                                                        investigationId)
                                                        .agentType("MAIN_AGENT")
                                                        .summary("RCA Investigation Completed")
                                                        .evidence(
                                                                        Map.of(
                                                                                        "issue", issue,
                                                                                        "namespace",
                                                                                        namespace,
                                                                                        "report",
                                                                                        report))
                                                        .createdAt(
                                                                        LocalDateTime.now())
                                                        .build());

                        investigationService.updateResult(
                                        investigationId,
                                        "Generated From AI Investigation",
                                        90.0,
                                        report);

                        investigationService.markCompleted(
                                        investigationId);
                        log.info("Investigation {} marked as COMPLETED", investigationId);

                        Incident incident = Incident.builder()
                                        .investigationId(
                                                        investigationId)
                                        .incidentNumber(
                                                        "INC-" +
                                                                        System.currentTimeMillis())
                                        .severity("MEDIUM")
                                        .status("OPEN")
                                        .createdAt(
                                                        LocalDateTime.now())
                                        .build();

                        incidentService.create(incident);
                        log.debug("Opened new incident {} for investigation {}", incident.getIncidentNumber(),
                                        investigationId);

                        log.info(
                                        "Investigation completed. InvestigationId={}",
                                        investigationId);

                        return report;

                } catch (Exception ex) {

                        investigationService.markFailed(
                                        investigationId);

                        log.error("AI Investigation failed for investigationId: {}. Issue: {}. Error: {}",
                                        investigationId,
                                        issue,
                                        ex.getMessage(),
                                        ex);

                        throw ex;
                }
        }
}