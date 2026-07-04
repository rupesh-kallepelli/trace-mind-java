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
        log.info("Listing all configured databases");
        return new ArrayList<>(registry.getDatabaseNames());
    }

    @Override
    public Map<String, Object> databaseHealth(
            String databaseName) {

        log.info("Checking health for database: {}", databaseName);
        try {

            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);

            try (Connection conn = jdbc.getDataSource().getConnection()) {

                DatabaseMetaData md = conn.getMetaData();

                Map<String, Object> result = new LinkedHashMap<>();

                result.put("database", databaseName);
                result.put("product", md.getDatabaseProductName());
                result.put("version", md.getDatabaseProductVersion());
                result.put("driver", md.getDriverName());

                return result;
            }
        } catch (Exception e) {
            log.error("Failed to get database health for: {}", databaseName, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> listTables(
            String databaseName) {

        log.info("Listing tables for database: {}", databaseName);
        try {

            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);

            try (Connection conn = jdbc.getDataSource().getConnection()) {

                DatabaseMetaData meta = conn.getMetaData();

                ResultSet rs = meta.getTables(
                        null,
                        null,
                        "%",
                        new String[] { "TABLE" });

                List<String> tables = new ArrayList<>();

                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME"));
                }

                return tables;
            }
        } catch (Exception e) {
            log.error("Failed to list tables for database: {}", databaseName, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> listViews(
            String databaseName) {

        log.info("Listing views for database: {}", databaseName);
        try {

            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);

            try (Connection conn = jdbc.getDataSource().getConnection()) {

                DatabaseMetaData meta = conn.getMetaData();

                ResultSet rs = meta.getTables(
                        null,
                        null,
                        "%",
                        new String[] { "VIEW" });

                List<String> views = new ArrayList<>();

                while (rs.next()) {
                    views.add(rs.getString("TABLE_NAME"));
                }

                return views;
            }
        } catch (Exception e) {
            log.error("Failed to list views for database: {}", databaseName, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, Object>> getTableSchema(
            String databaseName,
            String tableName) {

        log.info("Getting schema for table: {} in database: {}", tableName, databaseName);
        validateIdentifier(tableName);
        try {

            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);

            try (Connection conn = jdbc.getDataSource().getConnection()) {

                DatabaseMetaData meta = conn.getMetaData();

                ResultSet rs = meta.getColumns(
                        null,
                        null,
                        tableName,
                        "%");

                List<Map<String, Object>> result = new ArrayList<>();

                while (rs.next()) {

                    Map<String, Object> row = new LinkedHashMap<>();

                    row.put(
                            "column",
                            rs.getString("COLUMN_NAME"));

                    row.put(
                            "type",
                            rs.getString("TYPE_NAME"));

                    row.put(
                            "nullable",
                            rs.getString("IS_NULLABLE"));

                    result.add(row);
                }

                return result;
            }
        } catch (Exception e) {
            log.error("Failed to get table schema for: {}.{}", databaseName, tableName, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, Object>> getForeignKeys(
            String databaseName,
            String tableName) {

        log.info("Getting foreign keys for table: {} in database: {}", tableName, databaseName);
        validateIdentifier(tableName);
        try {

            JdbcTemplate jdbc = registry.getJdbcTemplate(databaseName);

            try (Connection conn = jdbc.getDataSource().getConnection()) {

                DatabaseMetaData meta = conn.getMetaData();

                ResultSet rs = meta.getImportedKeys(
                        null,
                        null,
                        tableName);

                List<Map<String, Object>> result = new ArrayList<>();

                while (rs.next()) {

                    Map<String, Object> row = new LinkedHashMap<>();

                    row.put(
                            "fkColumn",
                            rs.getString("FKCOLUMN_NAME"));

                    row.put(
                            "pkTable",
                            rs.getString("PKTABLE_NAME"));

                    row.put(
                            "pkColumn",
                            rs.getString("PKCOLUMN_NAME"));

                    result.add(row);
                }

                return result;
            }
        } catch (Exception e) {
            log.error("Failed to get foreign keys for: {}.{}", databaseName, tableName, e);
            throw new RuntimeException(e);
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