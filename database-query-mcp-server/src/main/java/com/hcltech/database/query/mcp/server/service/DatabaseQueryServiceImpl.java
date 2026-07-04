package com.hcltech.database.query.mcp.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hcltech.database.query.mcp.server.registry.DataSourceRegistry;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseQueryServiceImpl
        implements DatabaseQueryService {

    private final DataSourceRegistry registry;

    @Override
    public long countRows(
            String databaseName,
            String tableName) {
        log.info("Counting rows in table: {} for database: {}", tableName, databaseName);
        validateIdentifier(tableName);

        JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);

        String sql = "select count(*) from " + tableName;
        log.debug("Executing SQL: {}", sql);

        Long count = jdbc.queryForObject(
                sql,
                Long.class);

        return count == null ? 0 : count;
    }

    @Override
    public List<Map<String, Object>> searchData(
            String databaseName,
            String tableName,
            String columnName,
            String value) {
        log.info("Searching data in {}.{} where {} = ?", databaseName, tableName, columnName);
        validateIdentifier(tableName);
        validateIdentifier(columnName);

        JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);

        String sql = """
                select *
                from %s
                where %s = ?
                limit 100
                """.formatted(
                tableName,
                columnName);

        log.debug("Executing SQL: {} with value: {}", sql, value);

        return jdbc.queryForList(
                sql,
                value);
    }

    @Override
    public List<Map<String, Object>> executeSelect(
            String databaseName,
            String sql) {
        log.info("Executing custom SELECT query on database: {}", databaseName);

        validate(sql);

        JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);

        log.debug("Executing SQL: {}", sql);
        return jdbc.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> sampleRows(
            String databaseName,
            String tableName,
            Integer limit) {
        log.info("Fetching {} sample rows from {}.{}", limit, databaseName, tableName);
        validateIdentifier(tableName);

        JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);

        String sql = "select * from " +
                tableName +
                " limit " +
                limit;
        
        log.debug("Executing SQL: {}", sql);
        return jdbc.queryForList(sql);
    }

    private void validate(String sql) {
        if (sql == null || sql.isBlank()) {
            throw new IllegalArgumentException("SQL statement cannot be null or empty");
        }

        String normalized = sql.trim().toLowerCase();

        if (!normalized.startsWith("select")) {
            log.warn("Rejected non-SELECT statement: {}", sql);
            throw new IllegalArgumentException(
                    "Only SELECT statements allowed");
        }
    }

    private void validateIdentifier(String identifier) {
        if (identifier == null || !identifier.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            log.error("Invalid database identifier detected: {}", identifier);
            throw new IllegalArgumentException(
                    "Invalid table or column name: " + identifier);
        }
    }
}