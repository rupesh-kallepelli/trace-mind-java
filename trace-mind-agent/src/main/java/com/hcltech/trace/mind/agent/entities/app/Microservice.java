package com.hcltech.trace.mind.agent.entities.app;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "microservices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Microservice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Application application;

    private String name;

    private String serviceCode;

    private String language;

    private String framework;

    private Integer port;

    private String repositoryUrl;

    private String healthEndpoint;

    private String metricsEndpoint;

    private String version;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String businessCapability;

    @Column(columnDefinition = "TEXT")
    private String serviceContext;
}