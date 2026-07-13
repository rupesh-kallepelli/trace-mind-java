package com.hcltech.trace.mind.agent.entities.app;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "observability_sources")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObservabilitySource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Application application;

    private String sourceType;

    private String endpoint;

    @Column(columnDefinition = "TEXT")
    private String description;
}