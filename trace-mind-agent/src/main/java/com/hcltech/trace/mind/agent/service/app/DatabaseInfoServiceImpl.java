package com.hcltech.trace.mind.agent.service.app;

import com.hcltech.trace.mind.agent.entities.app.DatabaseInfo;
import com.hcltech.trace.mind.agent.repository.app.DatabaseInfoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseInfoServiceImpl implements DatabaseInfoService {

    private final DatabaseInfoRepository repository;

    @Override
    public DatabaseInfo create(DatabaseInfo database) {
        return repository.save(database);
    }

    @Override
    public DatabaseInfo update(Long id, DatabaseInfo database) {
        DatabaseInfo existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Database not found"));
        existing.setName(database.getName());
        existing.setDbType(database.getDbType());
        existing.setVersion(database.getVersion());
        existing.setHost(database.getHost());
        existing.setPort(database.getPort());
        existing.setEnvironment(database.getEnvironment());
        existing.setDescription(database.getDescription());
        existing.setDatabaseContext(database.getDatabaseContext());
        return repository.save(existing);
    }

    @Override
    public DatabaseInfo getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Database not found"));
    }

    @Override
    public List<DatabaseInfo> getAll() {
        return repository.findAll();
    }

    @Override
    public List<DatabaseInfo> getByApplication(Long applicationId) {
        return repository.findByApplicationId(applicationId);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}