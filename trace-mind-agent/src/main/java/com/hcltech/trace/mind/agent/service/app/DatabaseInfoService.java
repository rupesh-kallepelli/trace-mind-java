package com.hcltech.trace.mind.agent.service.app;

import com.hcltech.trace.mind.agent.entities.app.DatabaseInfo;

import java.util.List;

public interface DatabaseInfoService {

    DatabaseInfo create(DatabaseInfo database);

    DatabaseInfo update(Long id,
                        DatabaseInfo database);

    DatabaseInfo getById(Long id);

    List<DatabaseInfo> getAll();

    List<DatabaseInfo> getByApplication(Long applicationId);

    void delete(Long id);
}