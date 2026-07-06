package com.hcltech.database.query.mcp.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hcltech.database.query.mcp.server.registry.DataSourceRegistry;

import java.sql.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseMetadataServiceImpl
        implements DatabaseMetadataService {

    private final DataSourceRegistry registry;

    private static final String IDENTIFIER_PATTERN = "^[a-zA-Z_][a-zA-Z0-9_]*$";

    @Override
    public List<String> listDatabases() {
        log.info("Metadata Service: listDatabases called");
        List<String> databases = new ArrayList<>(registry.getDatabaseNames());
        log.debug("Metadata Service: found {} databases", databases.size());
        return databases;
    }

    @Override
    public Map<String, Object> databaseHealth(String databaseName) {
        log.info("Metadata Service: databaseHealth for {}", databaseName);
        try {
            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);
            try (Connection conn = jdbc.getDataSource().getConnection()) {
                DatabaseMetaData md = conn.getMetaData();
                Map<String, Object> result = new LinkedHashMap<>();
                result.put("database", databaseName);
                result.put("product", md.getDatabaseProductName());
                result.put("version", md.getDatabaseProductVersion());
                result.put("driver", md.getDriverName());
                log.debug("Metadata Service: databaseHealth result: {}", result);
                return result;
            }
        } catch (Exception e) {
            log.error("Metadata Service: databaseHealth failed for {}: {}", databaseName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> listTables(String databaseName) {
        log.info("Metadata Service: listTables for {}", databaseName);
        try {
            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);
            try (Connection conn = jdbc.getDataSource().getConnection()) {
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet rs = meta.getTables(null, null, "%", new String[] { "TABLE" });
                List<String> tables = new ArrayList<>();
                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME"));
                }
                log.debug("Metadata Service: found {} tables in {}", tables.size(), databaseName);
                return tables;
            }
        } catch (Exception e) {
            log.error("Metadata Service: listTables failed for {}: {}", databaseName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> listViews(String databaseName) {
        log.info("Metadata Service: listViews for {}", databaseName);
        try {
            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);
            try (Connection conn = jdbc.getDataSource().getConnection()) {
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet rs = meta.getTables(null, null, "%", new String[] { "VIEW" });
                List<String> views = new ArrayList<>();
                while (rs.next()) {
                    views.add(rs.getString("TABLE_NAME"));
                }
                log.debug("Metadata Service: found {} views in {}", views.size(), databaseName);
                return views;
            }
        } catch (Exception e) {
            log.error("Metadata Service: listViews failed for {}: {}", databaseName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, Object>> getTableSchema(String databaseName, String tableName) {
        log.info("Metadata Service: getTableSchema for {}.{}", databaseName, tableName);
        validateIdentifier(tableName);
        try {
            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);
            try (Connection conn = jdbc.getDataSource().getConnection()) {
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet rs = meta.getColumns(null, null, tableName, "%");
                List<Map<String, Object>> result = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("column", rs.getString("COLUMN_NAME"));
                    row.put("type", rs.getString("TYPE_NAME"));
                    row.put("nullable", rs.getString("IS_NULLABLE"));
                    result.add(row);
                }
                log.debug("Metadata Service: getTableSchema result size: {}", result.size());
                return result;
            }
        } catch (Exception e) {
            log.error("Metadata Service: getTableSchema failed for {}.{}: {}", databaseName, tableName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, Object>> getForeignKeys(String databaseName, String tableName) {
        log.info("Metadata Service: getForeignKeys for {}.{}", databaseName, tableName);
        validateIdentifier(tableName);
        try {
            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);
            try (Connection conn = jdbc.getDataSource().getConnection()) {
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet rs = meta.getImportedKeys(null, null, tableName);
                List<Map<String, Object>> result = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("fkColumn", rs.getString("FKCOLUMN_NAME"));
                    row.put("pkTable", rs.getString("PKTABLE_NAME"));
                    row.put("pkColumn", rs.getString("PKCOLUMN_NAME"));
                    result.add(row);
                }
                log.debug("Metadata Service: getForeignKeys found {} keys", result.size());
                return result;
            }
        } catch (Exception e) {
            log.error("Metadata Service: getForeignKeys failed for {}.{}: {}", databaseName, tableName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, Object>> getIndexes(String databaseName, String tableName) {
        log.info("Metadata Service: getIndexes for {}.{}", databaseName, tableName);
        try {
            validateIdentifier(tableName);
            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);
            try (Connection conn = jdbc.getDataSource().getConnection()) {
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet rs = meta.getIndexInfo(null, null, tableName, false, false);
                List<Map<String, Object>> results = new ArrayList<>();
                while (rs.next()) {
                    String indexName = rs.getString("INDEX_NAME");
                    if (indexName == null) continue;
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("indexName", indexName);
                    row.put("columnName", rs.getString("COLUMN_NAME"));
                    row.put("unique", !rs.getBoolean("NON_UNIQUE"));
                    results.add(row);
                }
                log.debug("Metadata Service: getIndexes result size: {}", results.size());
                return results;
            }
        } catch (Exception e) {
            log.error("Metadata Service: getIndexes failed for {}.{}: {}", databaseName, tableName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, Object>> getConstraints(String databaseName, String tableName) {
        log.info("Metadata Service: getConstraints for {}.{}", databaseName, tableName);
        try {
            validateIdentifier(tableName);
            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);
            String sql = """
                    SELECT tc.constraint_name, tc.constraint_type
                    FROM information_schema.table_constraints tc
                    WHERE tc.table_name = ?
                    """;
            log.debug("Executing SQL: {} with value: {}", sql, tableName);
            List<Map<String, Object>> result = jdbc.queryForList(sql, tableName);
            log.debug("Metadata Service: getConstraints result size: {}", result.size());
            return result;
        } catch (Exception e) {
            log.error("Metadata Service: getConstraints failed for {}.{}: {}", databaseName, tableName, e.getMessage());
            throw e;
        }
    }

    @Override
    public Map<String, Object> relationshipGraph(String databaseName, String tableName) {
        log.info("Metadata Service: relationshipGraph for {}.{}", databaseName, tableName);
        try {
            validateIdentifier(tableName);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("table", tableName);
            result.put("foreignKeys", getForeignKeys(databaseName, tableName));
            log.debug("Metadata Service: relationshipGraph result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Metadata Service: relationshipGraph failed: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Map<String, Object> tableStatistics(String databaseName, String tableName) {
        log.info("Metadata Service: tableStatistics for {}.{}", databaseName, tableName);
        try {
            validateIdentifier(tableName);
            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);
            Long count = jdbc.queryForObject("SELECT COUNT(*) FROM " + tableName, Long.class);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("table", tableName);
            result.put("rowCount", count);
            log.debug("Metadata Service: tableStatistics result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Metadata Service: tableStatistics failed for {}.{}: {}", databaseName, tableName, e.getMessage());
            throw e;
        }
    }

    private void validateIdentifier(String identifier) {
        if (identifier == null || !identifier.matches(IDENTIFIER_PATTERN)) {
            log.error("Invalid database identifier detected: {}", identifier);
            throw new IllegalArgumentException(
                    "Invalid table or column name: " + identifier);
        }
    }
}