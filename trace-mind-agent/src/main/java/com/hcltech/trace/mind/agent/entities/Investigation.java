package com.hcltech.trace.mind.agent.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "investigations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Investigation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String serviceName;

    private String namespace;

    @Column(columnDefinition = "TEXT")
    private String issueDescription;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String rootCause;

    private Double confidenceScore;

    @Column(columnDefinition = "TEXT")
    private String reportMarkdown;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;
    
    @Column(name = "created_by")
    private String createdBy;
}