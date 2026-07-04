package com.hcltech.database.query.mcp.server.service;

import java.util.List;
import java.util.Map;

public interface DatabaseQueryService {

    long countRows(String databaseName, String tableName);

    List<Map<String, Object>> searchData(String databaseName, String tableName, String columnName, String value);

    List<Map<String, Object>> executeSelect(String databaseName, String sql);

    List<Map<String, Object>> sampleRows(String databaseName, String tableName, Integer limit);
}
