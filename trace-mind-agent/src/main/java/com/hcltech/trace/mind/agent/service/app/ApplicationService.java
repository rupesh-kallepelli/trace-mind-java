package com.hcltech.trace.mind.agent.service.app;

import com.hcltech.trace.mind.agent.entities.app.Application;

import java.util.List;

public interface ApplicationService {

    Application create(Application application);

    Application update(Long id, Application application);

    Application getById(Long id);

    List<Application> getAll();

    void delete(Long id);
}