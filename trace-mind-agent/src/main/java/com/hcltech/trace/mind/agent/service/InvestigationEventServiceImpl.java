package com.hcltech.trace.mind.agent.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hcltech.trace.mind.agent.entities.EventType;
import com.hcltech.trace.mind.agent.entities.InvestigationEvent;
import com.hcltech.trace.mind.agent.repository.InvestigationEventRepository;
import com.hcltech.trace.mind.agent.response.InvestigationEventResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvestigationEventServiceImpl
                implements InvestigationEventService {

        private final InvestigationEventRepository repository;
        private final InvestigationStreamService streamService;

        @Override
        public InvestigationEvent publish(
                        String investigationId,
                        EventType eventType,
                        String source,
                        String message,
                        String payload) {

                InvestigationEvent event = InvestigationEvent.builder()
                                .investigationId(investigationId)
                                .eventType(eventType)
                                .source(source)
                                .message(message)
                                .payload(payload)
                                .createdAt(Instant.now())
                                .build();

                InvestigationEvent eventSaved = repository.save(event);
                streamService.publish(investigationId, eventSaved);
                return eventSaved;
        }

        @Override
        public List<InvestigationEventResponse> getEvents(String investigationId) {

                return repository
                                .findByInvestigationIdOrderByCreatedAtAsc(investigationId)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        private InvestigationEventResponse toResponse(
                        InvestigationEvent event) {

                return InvestigationEventResponse.builder()
                                .id(event.getId())
                                .investigationId(event.getInvestigationId())
                                .eventType(event.getEventType())
                                .source(event.getSource())
                                .message(event.getMessage())
                                .payload(event.getPayload())
                                .createdAt(event.getCreatedAt())
                                .build();
        }
}