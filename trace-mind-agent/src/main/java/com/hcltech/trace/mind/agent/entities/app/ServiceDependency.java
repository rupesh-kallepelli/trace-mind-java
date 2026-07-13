package com.hcltech.trace.mind.agent.entities.app;

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
@Table(name = "service_dependencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Microservice sourceService;

    @ManyToOne
    private Microservice targetService;

    private String dependencyType; // REST, Kafka, DB, gRPC
}