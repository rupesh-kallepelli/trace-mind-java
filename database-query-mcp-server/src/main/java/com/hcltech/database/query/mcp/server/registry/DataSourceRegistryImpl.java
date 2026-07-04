package com.hcltech.database.query.mcp.server.registry;

import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSourceRegistryImpl implements DataSourceRegistry {

    private final Map<String, JdbcTemplate> jdbcTemplates;

    @Override
    public JdbcTemplate getJdbcTemplate(String databaseName) {
        log.debug("Retrieving JdbcTemplate for database: {}", databaseName);

        JdbcTemplate jdbcTemplate = jdbcTemplates.get(databaseName);

        if (jdbcTemplate == null) {
            log.error("Requested database configuration missing: {}", databaseName);
            throw new IllegalArgumentException(
                    "Database not configured: " + databaseName);
        }

        return jdbcTemplate;
    }

    @Override
    public Set<String> getDatabaseNames() {
        return jdbcTemplates.keySet();
    }
}