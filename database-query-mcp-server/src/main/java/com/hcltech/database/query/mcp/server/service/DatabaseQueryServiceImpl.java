package com.hcltech.database.query.mcp.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hcltech.database.query.mcp.server.registry.DataSourceRegistry;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
        log.info("Query Service: countRows for {}.{}", databaseName, tableName);
        try {
            validateIdentifier(tableName);
            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);

            String sql = "SELECT COUNT(*) FROM " + tableName;
            log.debug("Executing SQL: {}", sql);

            Long count = jdbc.queryForObject(sql, Long.class);
            long result = count == null ? 0 : count;
            log.debug("Query Service: countRows result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Query Service: countRows failed for {}.{}: {}", databaseName, tableName, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> searchData(
            String databaseName,
            String tableName,
            String columnName,
            String value) {
        log.info("Query Service: searchData in {}.{} where {} = ?", databaseName, tableName, columnName);
        try {
            validateIdentifier(tableName);
            validateIdentifier(columnName);

            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);

            String sql = """
                    SELECT *
                    FROM %s
                    WHERE %s = ?
                    LIMIT 100
                    """.formatted(tableName, columnName);

            log.debug("Executing SQL: {} with value: {}", sql, value);
            List<Map<String, Object>> result = jdbc.queryForList(sql, value);
            log.debug("Query Service: searchData found {} rows", result.size());
            return result;
        } catch (Exception e) {
            log.error("Query Service: searchData failed: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> executeSelect(
            String databaseName,
            String sql) {
        log.info("Query Service: executeSelect on {}", databaseName);
        try {
            validate(sql);
            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);
            log.debug("Executing SQL: {}", sql);
            List<Map<String, Object>> result = jdbc.queryForList(sql);
            log.debug("Query Service: executeSelect returned {} rows", result.size());
            return result;
        } catch (Exception e) {
            log.error("Query Service: executeSelect failed: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> sampleRows(
            String databaseName,
            String tableName,
            Integer limit) {
        log.info("Query Service: sampleRows for {}.{} (limit: {})", databaseName, tableName, limit);
        try {
            validateIdentifier(tableName);
            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);

            String sql = "SELECT * FROM " + tableName + " LIMIT " + limit;
            log.debug("Executing SQL: {}", sql);
            List<Map<String, Object>> result = jdbc.queryForList(sql);
            log.debug("Query Service: sampleRows returned {} rows", result.size());
            return result;
        } catch (Exception e) {
            log.error("Query Service: sampleRows failed: {}", e.getMessage());
            throw e;
        }
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

    @Override
    public List<Map<String, Object>> explainQuery(
            String databaseName,
            String sql) {
        log.info("Query Service: explainQuery for database: {}", databaseName);
        try {
            validate(sql);
            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);
            String explainSql = "EXPLAIN " + sql;
            log.debug("Executing SQL: {}", explainSql);
            List<Map<String, Object>> result = jdbc.queryForList(explainSql);
            log.debug("Query Service: explainQuery result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Query Service: explainQuery failed: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> findDuplicates(
            String databaseName,
            String tableName,
            String columnName) {
        log.info("Query Service: findDuplicates for {}.{}", tableName, columnName);
        try {
            validateIdentifier(tableName);
            validateIdentifier(columnName);

            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);

            String sql = """
                    SELECT
                        %s,
                        COUNT(*) duplicate_count
                    FROM %s
                    GROUP BY %s
                    HAVING COUNT(*) > 1
                    LIMIT 100
                    """
                    .formatted(columnName, tableName, columnName);

            log.debug("Executing SQL: {}", sql);
            List<Map<String, Object>> result = jdbc.queryForList(sql);
            log.debug("Query Service: findDuplicates found {} sets", result.size());
            return result;
        } catch (Exception e) {
            log.error("Query Service: findDuplicates failed: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Map<String, Object> searchAllTables(
            String databaseName,
            String value) {
        log.info("Query Service: searchAllTables in {} for '{}'", databaseName, value);
        try {
            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);
            Map<String, Object> result = new LinkedHashMap<>();
            List<String> tables = listAllTables(jdbc);

            for (String table : tables) {
                try {
                    List<String> columns = findSearchableColumns(jdbc, table);
                    for (String column : columns) {
                        try {
                            String sql = """
                                    SELECT *
                                    FROM %s
                                    WHERE CAST(%s AS VARCHAR) LIKE ?
                                    LIMIT 10
                                    """.formatted(table, column);

                            List<Map<String, Object>> rows = jdbc.queryForList(sql, "%" + value + "%");
                            if (!rows.isEmpty()) {
                                Map<String, Object> match = new LinkedHashMap<>();
                                match.put("table", table);
                                match.put("column", column);
                                match.put("count", rows.size());
                                match.put("records", rows);
                                result.put(table + "." + column, match);
                            }
                        } catch (Exception ignored) {}
                    }
                } catch (Exception ignored) {}
            }
            log.debug("Query Service: searchAllTables found matches in {} columns", result.size());
            return result;
        } catch (Exception e) {
            log.error("Query Service: searchAllTables failed: {}", e.getMessage());
            throw e;
        }
    }

    private List<String> listAllTables(JdbcTemplate jdbc) {
        return jdbc.execute((java.sql.Connection conn) -> {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, null, "%", new String[]{"TABLE"})) {
                List<String> tables = new ArrayList<>();
                while (rs.next()) tables.add(rs.getString("TABLE_NAME"));
                return tables;
            }
        });
    }

    private List<String> findSearchableColumns(JdbcTemplate jdbc, String table) {
        return jdbc.execute((java.sql.Connection conn) -> {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getColumns(null, null, table, "%")) {
                List<String> columns = new ArrayList<>();
                while (rs.next()) {
                    String type = rs.getString("TYPE_NAME").toUpperCase();
                    if (type.contains("CHAR") || type.contains("TEXT")) {
                        columns.add(rs.getString("COLUMN_NAME"));
                    }
                }
                return columns;
            }
        });
    }

    private void validateIdentifier(String identifier) {
        if (identifier == null || !identifier.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            log.error("Invalid database identifier detected: {}", identifier);
            throw new IllegalArgumentException(
                    "Invalid table or column name: " + identifier);
        }
    }
}