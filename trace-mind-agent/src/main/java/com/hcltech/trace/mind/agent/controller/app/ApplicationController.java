package com.hcltech.trace.mind.agent.controller.app;

import com.hcltech.trace.mind.agent.entities.app.Application;
import com.hcltech.trace.mind.agent.response.app.ApplicationContextResponse;
import com.hcltech.trace.mind.agent.service.app.ApplicationContextService;
import com.hcltech.trace.mind.agent.service.app.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService service;
    private final ApplicationContextService contextService;

    @PostMapping
    public Application create(@RequestBody Application application) {

        return service.create(application);
    }

    @GetMapping
    public List<Application> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Application getById(@PathVariable Long id) {

        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Application update(@PathVariable Long id, @RequestBody Application application) {

        return service.update(id, application);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        service.delete(id);
    }

    @GetMapping("/{id}/full-context")
    public ApplicationContextResponse getFullContext(@PathVariable Long id) {

        return contextService.buildContext(id);
    }
}