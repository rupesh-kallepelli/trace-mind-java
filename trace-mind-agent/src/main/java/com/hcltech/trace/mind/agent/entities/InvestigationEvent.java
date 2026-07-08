package com.hcltech.trace.mind.agent.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "investigation_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestigationEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String investigationId;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private String source;

    @Column(length = 5000)
    private String message;

    @Lob
    private String payload;

    private Instant createdAt;
}