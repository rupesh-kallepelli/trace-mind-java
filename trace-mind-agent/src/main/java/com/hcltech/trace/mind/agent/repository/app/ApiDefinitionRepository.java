package com.hcltech.trace.mind.agent.repository.app;

import com.hcltech.trace.mind.agent.entities.app.ApiDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiDefinitionRepository extends JpaRepository<ApiDefinition, Long> {
    List<ApiDefinition> findByMicroserviceId(Long microserviceId);

    List<ApiDefinition> findByMicroserviceIdIn(List<Long> ids);

}