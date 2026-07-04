package com.hcltech.database.query.mcp.server.registry;

import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;

public interface DataSourceRegistry {

    JdbcTemplate getJdbcTemplate(String databaseName);

    Set<String> getDatabaseNames();
}