package com.hcltech.trace.mind.agent.service.app;

import com.hcltech.trace.mind.agent.entities.app.ApiDefinition;

import java.util.List;

public interface ApiDefinitionService {

    ApiDefinition create(ApiDefinition api);

    ApiDefinition update(Long id, ApiDefinition api);

    ApiDefinition getById(Long id);

    List<ApiDefinition> getAll();

    void delete(Long id);
    List<ApiDefinition> getByMicroservice(Long microserviceId);
}