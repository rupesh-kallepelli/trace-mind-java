package com.hcltech.trace.mind.agent.repository.app;

import com.hcltech.trace.mind.agent.entities.app.DatabaseSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseSchemaRepository extends JpaRepository<DatabaseSchema, Long> {
    List<DatabaseSchema>
    findByDatabaseIdIn(List<Long> ids);
}