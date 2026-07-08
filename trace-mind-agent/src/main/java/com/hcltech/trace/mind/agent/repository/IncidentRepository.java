package com.hcltech.trace.mind.agent.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcltech.trace.mind.agent.entities.Incident;


@Repository
public interface IncidentRepository extends JpaRepository<Incident, String> {

    Optional<Incident> findByIncidentNumber(String incidentNumber);

    List<Incident> findByStatus(String status);

    List<Incident> findBySeverity(String severity);

    List<Incident> findByAssignedTo(String assignedTo);

    long countByStatus(String status);

    long countBySeverity(String severity);
}