package com.hcltech.trace.mind.agent.service.app;

import com.hcltech.trace.mind.agent.entities.app.DatabaseTable;

import java.util.List;

public interface DatabaseTableService {

    DatabaseTable create(DatabaseTable table);

    DatabaseTable update(Long id, DatabaseTable table);

    DatabaseTable getById(Long id);

    List<DatabaseTable> getAll();

    List<DatabaseTable> getBySchema(Long schemaId);

    void delete(Long id);
}