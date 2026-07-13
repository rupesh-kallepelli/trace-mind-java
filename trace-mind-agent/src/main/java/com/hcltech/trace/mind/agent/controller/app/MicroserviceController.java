package com.hcltech.trace.mind.agent.controller.app;

import com.hcltech.trace.mind.agent.entities.app.Microservice;
import com.hcltech.trace.mind.agent.service.app.MicroserviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/microservices")
@RequiredArgsConstructor
public class MicroserviceController {

    private final MicroserviceService service;

    @PostMapping
    public Microservice create(@RequestBody Microservice microservice) {

        return service.create(microservice);
    }

    @GetMapping
    public List<Microservice> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Microservice getById(@PathVariable Long id) {

        return service.getById(id);
    }

    @GetMapping("/application/{applicationId}")
    public List<Microservice> getByApplication(@PathVariable Long applicationId) {

        return service.getByApplication(applicationId);
    }

    @PutMapping("/{id}")
    public Microservice update(@PathVariable Long id, @RequestBody Microservice microservice) {

        return service.update(id, microservice);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        service.delete(id);
    }
}