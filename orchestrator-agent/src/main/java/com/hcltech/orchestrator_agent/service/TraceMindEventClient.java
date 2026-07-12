package com.hcltech.orchestrator_agent.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.hcltech.orchestrator_agent.request.CreateInvestigationEventRequest;
import com.hcltech.orchestrator_agent.request.EventType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraceMindEventClient {

        private final RestClient restClient;

        @Value("${tracemind.api.base-url}")
        private String traceMindUrl;

        public void publishEvent(
                        String investigationId,
                        EventType eventType,
                        String source,
                        String message,
                        String payload) {

                log.info("Publishing event of type {} for investigationId: {}", eventType, investigationId);
                log.debug("Event details - Source: {}, Message: {}, Payload: {}", source, message, payload);

                CreateInvestigationEventRequest request = CreateInvestigationEventRequest.builder()
                                .eventType(eventType)
                                .source(source)
                                .message(message)
                                .payload(payload)
                                .build();

                try {
                        restClient.post()
                                        .uri(traceMindUrl +
                                                        "/api/v1/investigations/" +
                                                        investigationId +
                                                        "/events")
                                        .body(request)
                                        .retrieve()
                                        .toBodilessEntity();
                        log.debug("Successfully published event for investigationId: {}", investigationId);
                } catch (Exception e) {
                        log.error("Failed to publish event for investigationId: {}. Error: {}", investigationId, e.getMessage(), e);
                }
        }
}