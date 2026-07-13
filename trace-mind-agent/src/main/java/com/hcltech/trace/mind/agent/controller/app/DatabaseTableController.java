package com.hcltech.trace.mind.agent.controller.app;

import com.hcltech.trace.mind.agent.entities.app.DatabaseTable;
import com.hcltech.trace.mind.agent.service.app.DatabaseTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
public class DatabaseTableController {

    private final DatabaseTableService service;

    @PostMapping
    public DatabaseTable create(
            @RequestBody DatabaseTable table) {

        return service.create(table);
    }

    @GetMapping
    public List<DatabaseTable> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public DatabaseTable getById(
            @PathVariable Long id) {

        return service.getById(id);
    }

    @GetMapping("/schema/{schemaId}")
    public List<DatabaseTable> getBySchema(
            @PathVariable Long schemaId) {

        return service.getBySchema(schemaId);
    }

    @PutMapping("/{id}")
    public DatabaseTable update(
            @PathVariable Long id,
            @RequestBody DatabaseTable table) {

        return service.update(id, table);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        service.delete(id);
    }
}