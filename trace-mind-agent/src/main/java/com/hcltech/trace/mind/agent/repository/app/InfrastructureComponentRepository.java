package com.hcltech.trace.mind.agent.repository.app;

import com.hcltech.trace.mind.agent.entities.app.InfrastructureComponent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfrastructureComponentRepository extends JpaRepository<InfrastructureComponent, Long> {
    List<InfrastructureComponent> findByApplicationId(Long applicationId);
}