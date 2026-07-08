package com.hcltech.trace.mind.agent.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InvestigationStreamService {

    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String investigationId) {
        log.info("New SSE subscription request for investigation: {}", investigationId);

        SseEmitter emitter = new SseEmitter(0L);

        emitters.computeIfAbsent(
                investigationId,
                id -> new CopyOnWriteArrayList<>())
                .add(emitter);

        emitter.onCompletion(() -> {
            log.info("SSE emitter completed for investigation: {}", investigationId);
            remove(investigationId, emitter);
        });

        emitter.onTimeout(() -> {
            log.info("SSE emitter timed out for investigation: {}", investigationId);
            remove(investigationId, emitter);
        });

        emitter.onError(ex -> {
            log.error("SSE emitter error for investigation {}: {}", investigationId, ex.getMessage());
            remove(investigationId, emitter);
        });

        return emitter;
    }

    public void publish(
            String investigationId,
            Object event) {

        List<SseEmitter> listeners = emitters.get(investigationId);

        if (listeners == null) {
            log.debug("No active listeners found for investigation: {}", investigationId);
            return;
        }

        log.debug("Publishing event to {} listeners for investigation: {}", listeners.size(), investigationId);
        listeners.removeIf(emitter -> {

            try {

                emitter.send(
                        SseEmitter.event()
                                .name("investigation-event")
                                .data(event));

                return false;

            } catch (Exception e) {

                log.error("Failed to send SSE event for investigation {}: {}", investigationId, e.getMessage());
                emitter.complete();

                return true;
            }
        });
    }

    private void remove(
            String investigationId,
            SseEmitter emitter) {
        log.debug("Removing emitter for investigation: {}", investigationId);
        emitters.getOrDefault(
                investigationId,
                List.of())
                .remove(emitter);
    }
}