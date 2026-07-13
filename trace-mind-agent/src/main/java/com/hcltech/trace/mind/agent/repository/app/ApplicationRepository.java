package com.hcltech.trace.mind.agent.repository.app;

import com.hcltech.trace.mind.agent.entities.app.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository
        extends JpaRepository<Application, Long> {
}