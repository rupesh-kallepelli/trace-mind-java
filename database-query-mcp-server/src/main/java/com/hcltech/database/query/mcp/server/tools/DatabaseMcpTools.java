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

        @McpTool(name = "database_list_databases", description = "List all available databases")
        public String listDatabases() throws Exception {
                log.info("MCP Tool: listDatabases called");
                try {
                        String result = objectMapper.writeValueAsString(metadataService.listDatabases());
                        log.debug("MCP Tool: listDatabases result: {}", result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: listDatabases failed: {}", e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_health", description = "Get database health and info")
        public String databaseHealth(
                        @McpToolParam(description = "Database") String databaseName) throws Exception {
                log.info("MCP Tool: databaseHealth called for {}", databaseName);
                try {
                        String result = objectMapper.writeValueAsString(metadataService.databaseHealth(databaseName));
                        log.debug("MCP Tool: databaseHealth result for {}: {}", databaseName, result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: databaseHealth failed for {}: {}", databaseName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_list_tables", description = "List all tables")
        public String listTables(
                        @McpToolParam(description = "Database") String databaseName) throws Exception {
                log.info("MCP Tool: listTables called for {}", databaseName);
                try {
                        String result = objectMapper.writeValueAsString(metadataService.listTables(databaseName));
                        log.debug("MCP Tool: listTables result for {}: {}", databaseName, result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: listTables failed for {}: {}", databaseName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_list_views", description = "List all views")
        public String listViews(
                        @McpToolParam(description = "Database") String databaseName) throws Exception {
                log.info("MCP Tool: listViews called for {}", databaseName);
                try {
                        String result = objectMapper.writeValueAsString(metadataService.listViews(databaseName));
                        log.debug("MCP Tool: listViews result for {}: {}", databaseName, result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: listViews failed for {}: {}", databaseName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_get_table_schema", description = "Get table schema")
        public String getTableSchema(
                        @McpToolParam(description = "Database") String databaseName,
                        @McpToolParam(description = "Table") String tableName) throws Exception {
                log.info("MCP Tool: getTableSchema called for {}.{}", databaseName, tableName);
                try {
                        String result = objectMapper.writeValueAsString(metadataService.getTableSchema(databaseName, tableName));
                        log.debug("MCP Tool: getTableSchema result for {}.{}: {}", databaseName, tableName, result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: getTableSchema failed for {}.{}: {}", databaseName, tableName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_get_foreign_keys", description = "Get foreign keys")
        public String getForeignKeys(
                        @McpToolParam(description = "Database") String databaseName,
                        @McpToolParam(description = "Table") String tableName) throws Exception {
                log.info("MCP Tool: getForeignKeys called for {}.{}", databaseName, tableName);
                try {
                        String result = objectMapper.writeValueAsString(metadataService.getForeignKeys(databaseName, tableName));
                        log.debug("MCP Tool: getForeignKeys result for {}.{}: {}", databaseName, tableName, result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: getForeignKeys failed for {}.{}: {}", databaseName, tableName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_execute_select", description = "Execute SELECT query")
        public String executeSelect(
                        @McpToolParam(description = "Database") String databaseName,
                        @McpToolParam(description = "SQL") String sql) throws Exception {
                log.info("MCP Tool: executeSelect called for {}", databaseName);
                try {
                        String result = objectMapper.writeValueAsString(queryService.executeSelect(databaseName, sql));
                        log.debug("MCP Tool: executeSelect result: {}", result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: executeSelect failed for {}: {}", databaseName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_count_rows", description = "Count table rows")
        public String countRows(
                        @McpToolParam(description = "Database") String databaseName,
                        @McpToolParam(description = "Table") String tableName) throws Exception {
                log.info("MCP Tool: countRows called for {}.{}", databaseName, tableName);
                try {
                        long count = queryService.countRows(databaseName, tableName);
                        String result = "Row count: " + count;
                        log.debug("MCP Tool: countRows result for {}.{}: {}", databaseName, tableName, result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: countRows failed for {}.{}: {}", databaseName, tableName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_sample_rows", description = "Get sample rows")
        public String sampleRows(
                        @McpToolParam(description = "Database") String databaseName,
                        @McpToolParam(description = "Table") String tableName,
                        @McpToolParam(description = "Limit") Integer limit) throws Exception {
                log.info("MCP Tool: sampleRows called for {}.{} (limit: {})", databaseName, tableName, limit);
                try {
                        String result = objectMapper.writeValueAsString(queryService.sampleRows(databaseName, tableName, limit));
                        log.debug("MCP Tool: sampleRows result: {}", result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: sampleRows failed for {}.{}: {}", databaseName, tableName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_search_data", description = "Search rows by column")
        public String searchData(
                        @McpToolParam(description = "Database") String databaseName,
                        @McpToolParam(description = "Table") String tableName,
                        @McpToolParam(description = "Column") String columnName,
                        @McpToolParam(description = "Value") String value) throws Exception {
                log.info("MCP Tool: searchData called for {}.{} ({}={})", databaseName, tableName, columnName, value);
                try {
                        String result = objectMapper.writeValueAsString(queryService.searchData(databaseName, tableName, columnName, value));
                        log.debug("MCP Tool: searchData result: {}", result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: searchData failed for {}.{}: {}", databaseName, tableName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_get_indexes", description = "Get table indexes")
        public String getIndexes(
                        @McpToolParam(description = "Database") String databaseName,
                        @McpToolParam(description = "Table") String tableName) throws Exception {
                log.info("MCP Tool: getIndexes called for {}.{}", databaseName, tableName);
                try {
                        String result = objectMapper.writeValueAsString(metadataService.getIndexes(databaseName, tableName));
                        log.debug("MCP Tool: getIndexes result: {}", result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: getIndexes failed for {}.{}: {}", databaseName, tableName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_get_constraints", description = "Get table constraints")
        public String getConstraints(
                        @McpToolParam(description = "Database") String databaseName,
                        @McpToolParam(description = "Table") String tableName) throws Exception {
                log.info("MCP Tool: getConstraints called for {}.{}", databaseName, tableName);
                try {
                        String result = objectMapper.writeValueAsString(metadataService.getConstraints(databaseName, tableName));
                        log.debug("MCP Tool: getConstraints result: {}", result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: getConstraints failed for {}.{}: {}", databaseName, tableName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_relationship_graph", description = "Get relationships")
        public String relationshipGraph(
                        @McpToolParam(description = "Database") String databaseName,
                        @McpToolParam(description = "Table") String tableName) throws Exception {
                log.info("MCP Tool: relationshipGraph called for {}.{}", databaseName, tableName);
                try {
                        String result = objectMapper.writeValueAsString(metadataService.relationshipGraph(databaseName, tableName));
                        log.debug("MCP Tool: relationshipGraph result: {}", result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: relationshipGraph failed for {}.{}: {}", databaseName, tableName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_table_statistics", description = "Get table stats")
        public String tableStatistics(
                        @McpToolParam(description = "Database") String databaseName,
                        @McpToolParam(description = "Table") String tableName) throws Exception {
                log.info("MCP Tool: tableStatistics called for {}.{}", databaseName, tableName);
                try {
                        String result = objectMapper.writeValueAsString(metadataService.tableStatistics(databaseName, tableName));
                        log.debug("MCP Tool: tableStatistics result: {}", result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: tableStatistics failed for {}.{}: {}", databaseName, tableName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_explain_query", description = "Explain query plan")
        public String explainQuery(
                        @McpToolParam(description = "Database") String databaseName,
                        @McpToolParam(description = "SQL") String sql) throws Exception {
                log.info("MCP Tool: explainQuery called for {}", databaseName);
                try {
                        String result = objectMapper.writeValueAsString(queryService.explainQuery(databaseName, sql));
                        log.debug("MCP Tool: explainQuery result: {}", result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: explainQuery failed for {}: {}", databaseName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_find_duplicates", description = "Find duplicate rows")
        public String findDuplicates(
                        @McpToolParam(description = "Database") String databaseName,
                        @McpToolParam(description = "Table") String tableName,
                        @McpToolParam(description = "Column") String columnName) throws Exception {
                log.info("MCP Tool: findDuplicates called for {}.{}", databaseName, tableName);
                try {
                        String result = objectMapper.writeValueAsString(queryService.findDuplicates(databaseName, tableName, columnName));
                        log.debug("MCP Tool: findDuplicates result: {}", result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: findDuplicates failed for {}.{}: {}", databaseName, tableName, e.getMessage(), e);
                        throw e;
                }
        }

        @McpTool(name = "database_search_all_tables", description = "Global search")
        public String searchAllTables(
                        @McpToolParam(description = "Database") String databaseName,
                        @McpToolParam(description = "Value") String value) throws Exception {
                log.info("MCP Tool: searchAllTables called for {}", databaseName);
                try {
                        String result = objectMapper.writeValueAsString(queryService.searchAllTables(databaseName, value));
                        log.debug("MCP Tool: searchAllTables result: {}", result);
                        return result;
                } catch (Exception e) {
                        log.error("MCP Tool: searchAllTables failed for {}: {}", databaseName, e.getMessage(), e);
                        throw e;
                }
        }
}