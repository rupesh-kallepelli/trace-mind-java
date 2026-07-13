package com.hcltech.trace.mind.agent.repository.app;

import com.hcltech.trace.mind.agent.entities.app.DatabaseTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseTableRepository extends JpaRepository<DatabaseTable, Long> {
    List<DatabaseTable> findBySchemaId(Long schemaId);

    List<DatabaseTable> findBySchemaIdIn(List<Long> ids);
}