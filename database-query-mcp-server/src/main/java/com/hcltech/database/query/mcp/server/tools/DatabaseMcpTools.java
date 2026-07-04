package com.hcltech.database.query.mcp.server.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.database.query.mcp.server.service.DatabaseMetadataService;
import com.hcltech.database.query.mcp.server.service.DatabaseQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatabaseMcpTools {

    private final DatabaseMetadataService metadataService;
    private final DatabaseQueryService queryService;
    private final ObjectMapper objectMapper;

    @McpTool(
            name = "database_list_databases",
            description = "List all configured databases")
    public String listDatabases() throws Exception {
        log.info("MCP Tool: listDatabases called");

        return objectMapper.writeValueAsString(
                metadataService.listDatabases());
    }

    @McpTool(
            name = "database_health",
            description = "Get database information and health")
    public String databaseHealth(
            @McpToolParam(description = "Database name")
            String databaseName) throws Exception {

        log.info("MCP Tool: databaseHealth called for {}", databaseName);
        return objectMapper.writeValueAsString(
                metadataService.databaseHealth(databaseName));
    }

    @McpTool(
            name = "database_list_tables",
            description = "List tables in a database")
    public String listTables(
            @McpToolParam(description = "Database name")
            String databaseName) throws Exception {

        log.info("MCP Tool: listTables called for {}", databaseName);
        return objectMapper.writeValueAsString(
                metadataService.listTables(databaseName));
    }

    @McpTool(
            name = "database_list_views",
            description = "List views in a database")
    public String listViews(
            @McpToolParam(description = "Database name")
            String databaseName) throws Exception {

        log.info("MCP Tool: listViews called for {}", databaseName);
        return objectMapper.writeValueAsString(
                metadataService.listViews(databaseName));
    }

    @McpTool(
            name = "database_get_table_schema",
            description = "Get schema information for a table")
    public String getTableSchema(

            @McpToolParam(description = "Database name")
            String databaseName,

            @McpToolParam(description = "Table name")
            String tableName) throws Exception {

        log.info("MCP Tool: getTableSchema called for {}.{}", databaseName, tableName);
        return objectMapper.writeValueAsString(
                metadataService.getTableSchema(
                        databaseName,
                        tableName));
    }

    @McpTool(
            name = "database_get_foreign_keys",
            description = "Get foreign key relationships for a table")
    public String getForeignKeys(

            @McpToolParam(description = "Database name")
            String databaseName,

            @McpToolParam(description = "Table name")
            String tableName) throws Exception {

        log.info("MCP Tool: getForeignKeys called for {}.{}", databaseName, tableName);
        return objectMapper.writeValueAsString(
                metadataService.getForeignKeys(
                        databaseName,
                        tableName));
    }

    @McpTool(
            name = "database_execute_select",
            description = "Execute read-only SELECT SQL query")
    public String executeSelect(

            @McpToolParam(description = "Database name")
            String databaseName,

            @McpToolParam(description = "SELECT SQL query")
            String sql) throws Exception {

        log.info("MCP Tool: executeSelect called for database {}", databaseName);
        return objectMapper.writeValueAsString(
                queryService.executeSelect(
                        databaseName,
                        sql));
    }

    @McpTool(
            name = "database_count_rows",
            description = "Count records in a table")
    public String countRows(

            @McpToolParam(description = "Database name")
            String databaseName,

            @McpToolParam(description = "Table name")
            String tableName) {

        log.info("MCP Tool: countRows called for {}.{}", databaseName, tableName);
        long count = queryService.countRows(
                databaseName,
                tableName);

        return "Row count: " + count;
    }

    @McpTool(
            name = "database_sample_rows",
            description = "Fetch sample records from a table")
    public String sampleRows(

            @McpToolParam(description = "Database name")
            String databaseName,

            @McpToolParam(description = "Table name")
            String tableName,

            @McpToolParam(description = "Number of rows")
            Integer limit) throws Exception {

        log.info("MCP Tool: sampleRows called for {}.{} (limit: {})", databaseName, tableName, limit);
        return objectMapper.writeValueAsString(
                queryService.sampleRows(
                        databaseName,
                        tableName,
                        limit));
    }

    @McpTool(
            name = "database_search_data",
            description = "Search records based on column value")
    public String searchData(

            @McpToolParam(description = "Database name")
            String databaseName,

            @McpToolParam(description = "Table name")
            String tableName,

            @McpToolParam(description = "Column name")
            String columnName,

            @McpToolParam(description = "Column value")
            String value) throws Exception {

        log.info("MCP Tool: searchData called for {}.{} where {} = {}", databaseName, tableName, columnName, value);
        return objectMapper.writeValueAsString(
                queryService.searchData(
                        databaseName,
                        tableName,
                        columnName,
                        value));
    }
}