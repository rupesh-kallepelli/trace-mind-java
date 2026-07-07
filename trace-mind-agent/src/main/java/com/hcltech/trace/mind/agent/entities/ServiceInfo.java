package com.hcltech.trace.mind.agent.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.google.auto.value.AutoValue.Builder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceInfo {

    @Id
    @GeneratedValue
    private UUID id;

    private String serviceName;

    private String namespace;

    private String ownerTeam;

    private String deploymentName;

    private LocalDateTime createdAt;
}