package com.hcltech.trace.mind.agent.service.app;

import com.hcltech.trace.mind.agent.entities.app.Microservice;

import java.util.List;

public interface MicroserviceService {

    Microservice create(Microservice microservice);

    Microservice update(Long id, Microservice microservice);

    Microservice getById(Long id);

    List<Microservice> getAll();

    List<Microservice> getByApplication(Long applicationId);

    void delete(Long id);
}