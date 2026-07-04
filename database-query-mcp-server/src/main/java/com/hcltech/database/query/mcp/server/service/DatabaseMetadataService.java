package com.hcltech.database.query.mcp.server.service;

import java.util.List;
import java.util.Map;

public interface DatabaseMetadataService {

    List<String> listDatabases();

    Map<String, Object> databaseHealth(String databaseName);

    List<String> listTables(String databaseName);

    List<String> listViews(String databaseName);

    List<Map<String, Object>> getTableSchema(String databaseName, String tableName);

    List<Map<String, Object>> getForeignKeys(String databaseName, String tableName);
}
