package com.hcltech.trace.mind.agent.controller.app;

import com.hcltech.trace.mind.agent.entities.app.ApiDefinition;
import com.hcltech.trace.mind.agent.service.app.ApiDefinitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/apis")
@RequiredArgsConstructor
public class ApiDefinitionController {

    private final ApiDefinitionService service;

    @PostMapping
    public ApiDefinition create(@RequestBody ApiDefinition api) {

        return service.create(api);
    }

    @GetMapping
    public List<ApiDefinition> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ApiDefinition getById(@PathVariable Long id) {

        return service.getById(id);
    }

    @GetMapping("/microservice/{microserviceId}")
    public List<ApiDefinition> getByMicroservice(@PathVariable Long microserviceId) {

        return service.getByMicroservice(microserviceId);
    }

    @PutMapping("/{id}")
    public ApiDefinition update(@PathVariable Long id, @RequestBody ApiDefinition api) {

        return service.update(id, api);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        service.delete(id);
    }
}