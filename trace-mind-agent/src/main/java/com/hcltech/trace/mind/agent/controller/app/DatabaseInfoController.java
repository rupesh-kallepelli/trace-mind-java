package com.hcltech.trace.mind.agent.controller.app;

import com.hcltech.trace.mind.agent.entities.app.DatabaseInfo;
import com.hcltech.trace.mind.agent.service.app.DatabaseInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/databases")
@RequiredArgsConstructor
public class DatabaseInfoController {

    private final DatabaseInfoService service;

    @PostMapping
    public DatabaseInfo create(@RequestBody DatabaseInfo database) {

        return service.create(database);
    }

    @GetMapping
    public List<DatabaseInfo> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public DatabaseInfo getById(@PathVariable Long id) {

        return service.getById(id);
    }

    @GetMapping("/application/{applicationId}")
    public List<DatabaseInfo> getByApplication(@PathVariable Long applicationId) {

        return service.getByApplication(applicationId);
    }

    @PutMapping("/{id}")
    public DatabaseInfo update(@PathVariable Long id, @RequestBody DatabaseInfo database) {

        return service.update(id, database);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        service.delete(id);
    }
}