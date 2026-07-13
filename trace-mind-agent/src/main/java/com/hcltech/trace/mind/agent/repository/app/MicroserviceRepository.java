package com.hcltech.trace.mind.agent.repository.app;

import com.hcltech.trace.mind.agent.entities.app.Microservice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MicroserviceRepository extends JpaRepository<Microservice, Long> {
    List<Microservice> findByApplicationId(Long applicationId);
}