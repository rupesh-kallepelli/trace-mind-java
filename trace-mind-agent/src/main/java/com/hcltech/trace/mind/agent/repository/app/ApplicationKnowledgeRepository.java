package com.hcltech.trace.mind.agent.repository.app;

import com.hcltech.trace.mind.agent.entities.app.Application;
import com.hcltech.trace.mind.agent.entities.app.ApplicationKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationKnowledgeRepository extends JpaRepository<ApplicationKnowledge, Long> {
    List<ApplicationKnowledge>
    findByApplicationId(Long applicationId);
}