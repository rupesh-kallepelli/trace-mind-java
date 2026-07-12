package com.hcltech.trace.mind.agent.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.hcltech.trace.mind.agent.entities.InvestigationEvent;
import com.hcltech.trace.mind.agent.request.CreateInvestigationEventRequest;
import com.hcltech.trace.mind.agent.request.CreateInvestigationRequest;
import com.hcltech.trace.mind.agent.response.InvestigationEventResponse;
import com.hcltech.trace.mind.agent.response.InvestigationResponse;
import com.hcltech.trace.mind.agent.response.InvestigationSummaryResponse;
import com.hcltech.trace.mind.agent.service.InvestigationEventService;
import com.hcltech.trace.mind.agent.service.InvestigationService;
import com.hcltech.trace.mind.agent.service.InvestigationStreamService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/investigations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InvestigationController {

        private final InvestigationService investigationService;
        private final InvestigationEventService eventService;
        private final InvestigationStreamService streamService;

        @PostMapping
        public ResponseEntity<InvestigationResponse> create(
                        @Valid @RequestBody CreateInvestigationRequest request) {

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(investigationService.create(request));
        }

        @GetMapping("/{id}")
        public ResponseEntity<InvestigationResponse> getById(
                        @PathVariable String id) {

                return ResponseEntity.ok(
                                investigationService.getById(id));
        }

        @GetMapping
        public ResponseEntity<Page<InvestigationSummaryResponse>> getAll(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "20") int size) {

                return ResponseEntity.ok(
                                investigationService.getAll(page, size));
        }

        @PutMapping("/{id}/running")
        public ResponseEntity<Void> markRunning(
                        @PathVariable String id) {

                investigationService.markRunning(id);

                return ResponseEntity.ok().build();
        }

        @PutMapping("/{id}/completed")
        public ResponseEntity<Void> markCompleted(
                        @PathVariable String id) {

                investigationService.markCompleted(id);

                return ResponseEntity.ok().build();
        }

        @PutMapping("/{id}/failed")
        public ResponseEntity<Void> markFailed(
                        @PathVariable String id) {

                investigationService.markFailed(id);

                return ResponseEntity.ok().build();
        }

        @GetMapping("/{id}/events")
        public ResponseEntity<List<InvestigationEventResponse>> getEvents(
                        @PathVariable String id) {

                return ResponseEntity.ok(
                                eventService.getEvents(id));
        }

        @PostMapping("/{id}/events")
        public ResponseEntity<InvestigationEvent> createEvent(
                        @PathVariable String id,
                        @RequestBody CreateInvestigationEventRequest request) {

                return ResponseEntity.ok(
                                eventService.publish(
                                                id,
                                                request.getEventType(),
                                                request.getSource(),
                                                request.getMessage(),
                                                request.getPayload()));
        }

        @GetMapping(value = "/{id}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public SseEmitter stream(
                        @PathVariable String id) {

                return streamService.subscribe(id);
        }
}