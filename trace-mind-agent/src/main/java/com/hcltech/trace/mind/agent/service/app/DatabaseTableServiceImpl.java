package com.hcltech.trace.mind.agent.service.app;

import com.hcltech.trace.mind.agent.entities.app.DatabaseTable;
import com.hcltech.trace.mind.agent.repository.app.DatabaseTableRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseTableServiceImpl implements DatabaseTableService {

    private final DatabaseTableRepository repository;

    @Override
    public DatabaseTable create(DatabaseTable table) {
        return repository.save(table);
    }

    @Override
    public DatabaseTable update(Long id, DatabaseTable table) {
        DatabaseTable existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Database Table not found"));
        existing.setTableName(table.getTableName());
        existing.setDescription(table.getDescription());
        existing.setBusinessContext(table.getBusinessContext());
        existing.setInvestigationContext(table.getInvestigationContext());
        return repository.save(existing);
    }

    @Override
    public DatabaseTable getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Database Table not found"));
    }

    @Override
    public List<DatabaseTable> getAll() {
        return repository.findAll();
    }

    @Override
    public List<DatabaseTable> getBySchema(Long schemaId) {
        return repository.findBySchemaId(schemaId);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}