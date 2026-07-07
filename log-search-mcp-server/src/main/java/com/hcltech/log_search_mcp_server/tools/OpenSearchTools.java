package com.hcltech.log_search_mcp_server.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.log_search_mcp_server.service.OpenSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.index.query.BoolQueryBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHit;
import org.opensearch.search.aggregations.AggregationBuilders;
import org.opensearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.opensearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.opensearch.search.aggregations.bucket.terms.Terms;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.search.sort.SortOrder;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class OpenSearchTools {

    private final OpenSearchService service;
    private final ObjectMapper objectMapper;

    @McpTool(name = "opensearch_search_logs_by_text", description = "Search logs by text")
    public String searchLogsByText(
            @McpToolParam(description = "Query") String query) throws Exception {
        log.info("MCP Tool: searchLogsByText called with: {}", query);
        try {
            SearchSourceBuilder source = new SearchSourceBuilder()
                    .query(QueryBuilders.queryStringQuery(query))
                    .size(100);
            log.debug("Executing OpenSearch query: {}", source);
            String result = buildSearchResponse(service.search(source));
            log.debug("MCP Tool: searchLogsByText result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("MCP Tool: searchLogsByText failed: {}", e.getMessage(), e);
            return e.getMessage();
        }
    }

    @McpTool(name = "opensearch_search_logs_by_timerange", description = "Search logs by time range")
    public String searchLogsByTimeRange(
            @McpToolParam(description = "Start") String startTime,
            @McpToolParam(description = "End") String endTime) throws Exception {
        log.info("MCP Tool: searchLogsByTimeRange called from {} to {}", startTime, endTime);
        try {
            SearchSourceBuilder source = new SearchSourceBuilder()
                    .query(
                            QueryBuilders.rangeQuery("@timestamp")
                                    .gte(startTime)
                                    .lte(endTime))
                    .size(100);
            log.debug("Executing OpenSearch query: {}", source);
            String result = buildSearchResponse(service.search(source));
            log.debug("MCP Tool: searchLogsByTimeRange result size: {}", result.length());
            return result;
        } catch (Exception e) {
            log.error("MCP Tool: searchLogsByTimeRange failed: {}", e.getMessage(), e);
            return e.getMessage();
        }
    }

    @McpTool(name = "opensearch_search_logs", description = "Search logs by query and time")
    public String searchLogs(
            @McpToolParam(description = "Query") String query,
            @McpToolParam(description = "Start") String startTime,
            @McpToolParam(description = "End") String endTime) throws Exception {
        log.info("MCP Tool: searchLogs called for '{}' [{} - {}]", query, startTime, endTime);
        try {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                    .must(QueryBuilders.queryStringQuery(query))
                    .filter(
                            QueryBuilders.rangeQuery("@timestamp")
                                    .gte(startTime)
                                    .lte(endTime));

            SearchSourceBuilder source = new SearchSourceBuilder()
                    .query(boolQuery)
                    .size(100);

            log.debug("Executing OpenSearch query: {}", source);
            String result = buildSearchResponse(service.search(source));
            log.debug("MCP Tool: searchLogs result size: {}", result.length());
            return result;
        } catch (Exception e) {
            log.error("MCP Tool: searchLogs failed: {}", e.getMessage(), e);
            return e.getMessage();
        }
    }

    @McpTool(name = "opensearch_recent_errors", description = "Get recent errors")
    public String recentErrors() throws Exception {
        log.info("MCP Tool: recentErrors called");
        try {
            SearchSourceBuilder source = new SearchSourceBuilder()
                    .query(QueryBuilders.matchQuery("level", "ERROR"))
                    .sort("@timestamp", SortOrder.DESC)
                    .size(100);

            log.debug("Executing OpenSearch query: {}", source);
            String result = buildSearchResponse(service.search(source));
            log.debug("MCP Tool: recentErrors result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("MCP Tool: recentErrors failed: {}", e.getMessage(), e);
            return e.getMessage();
        }
    }

    @McpTool(name = "opensearch_trace_request", description = "Trace request by correlation ID")
    public String traceRequest(
            @McpToolParam(description = "ID") String correlationId) throws Exception {
        log.info("MCP Tool: traceRequest called for {}", correlationId);
        try {
            SearchSourceBuilder source = new SearchSourceBuilder()
                    .query(
                            QueryBuilders.termQuery(
                                    "correlationId.keyword",
                                    correlationId))
                    .sort(
                            "@timestamp",
                            SortOrder.ASC)
                    .size(1000);

            log.debug("Executing OpenSearch query: {}", source);
            SearchResponse response = service.search(source);
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put("correlationId", correlationId);
            resultMap.put("totalEvents", response.getHits().getTotalHits().value);

            List<Map<String, Object>> events = new ArrayList<>();
            for (SearchHit hit : response.getHits()) {
                events.add(hit.getSourceAsMap());
            }
            resultMap.put("events", events);

            String result = objectMapper.writeValueAsString(resultMap);
            log.debug("MCP Tool: traceRequest found {} events", events.size());
            return result;
        } catch (Exception e) {
            log.error("MCP Tool: traceRequest failed: {}", e.getMessage(), e);
            return e.getMessage();
        }
    }

    @McpTool(name = "opensearch_top_exceptions", description = "Get frequent exceptions")
    public String topExceptions() throws Exception {
        log.info("MCP Tool: topExceptions called");
        try {
            SearchSourceBuilder source = new SearchSourceBuilder()
                    .aggregation(
                            AggregationBuilders
                                    .terms("exceptions")
                                    .field("exception.keyword")
                                    .size(20))
                    .size(0);

            log.debug("Executing OpenSearch aggregation: {}", source);
            SearchResponse response = service.aggregate(source);
            Terms terms = response.getAggregations().get("exceptions");
            List<Map<String, Object>> buckets = new ArrayList<>();

            for (Terms.Bucket bucket : terms.getBuckets()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("exception", bucket.getKeyAsString());
                row.put("count", bucket.getDocCount());
                buckets.add(row);
            }
            String result = objectMapper.writeValueAsString(buckets);
            log.debug("MCP Tool: topExceptions found {} buckets", buckets.size());
            return result;
        } catch (Exception e) {
            log.error("MCP Tool: topExceptions failed: {}", e.getMessage(), e);
            return e.getMessage();
        }
    }

    @McpTool(name = "opensearch_exception_analysis", description = "Analyze specific exception")
    public String exceptionAnalysis(
            @McpToolParam(description = "Name") String exception) throws Exception {
        log.info("MCP Tool: exceptionAnalysis called for {}", exception);
        try {
            SearchSourceBuilder source = new SearchSourceBuilder()
                    .query(QueryBuilders.matchQuery("exception", exception))
                    .size(100);
            log.debug("Executing OpenSearch query: {}", source);
            String result = buildSearchResponse(service.search(source));
            log.debug("MCP Tool: exceptionAnalysis result size: {}", result.length());
            return result;
        } catch (Exception e) {
            log.error("MCP Tool: exceptionAnalysis failed: {}", e.getMessage(), e);
            return e.getMessage();
        }
    }

    @McpTool(name = "opensearch_search_by_field", description = "Search logs by field")
    public String searchByField(
            @McpToolParam(description = "Field") String field,
            @McpToolParam(description = "Value") String value) throws Exception {
        log.info("MCP Tool: searchByField called for {}={}", field, value);
        try {
            SearchSourceBuilder source = new SearchSourceBuilder()
                    .query(QueryBuilders.matchQuery(field, value))
                    .size(100);
            log.debug("Executing OpenSearch query: {}", source);
            String result = buildSearchResponse(service.search(source));
            log.debug("MCP Tool: searchByField result size: {}", result.length());
            return result;
        } catch (Exception e) {
            log.error("MCP Tool: searchByField failed: {}", e.getMessage(), e);
            return e.getMessage();
        }
    }

    @McpTool(name = "opensearch_count_logs", description = "Count logs by query")
    public String countLogs(
            @McpToolParam(description = "Query") String query) throws Exception {
        log.info("MCP Tool: countLogs called for query: {}", query);
        try {
            Map<String, Object> resultMap = new LinkedHashMap<>();
            log.debug("Executing OpenSearch count for: {}", query);
            resultMap.put("query", query);
            resultMap.put("count", service.count(query));
            String result = objectMapper.writeValueAsString(resultMap);
            log.debug("MCP Tool: countLogs result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("MCP Tool: countLogs failed: {}", e.getMessage(), e);
            return e.getMessage();
        }
    }

    @McpTool(name = "opensearch_list_services", description = "List log publishers")
    public String listServices() throws Exception {
        log.info("MCP Tool: listServices called");
        try {
            SearchSourceBuilder source = new SearchSourceBuilder()
                    .aggregation(
                            AggregationBuilders
                                    .terms("services")
                                    .field("service.keyword")
                                    .size(100))
                    .size(0);

            log.debug("Executing OpenSearch aggregation: {}", source);
            SearchResponse response = service.aggregate(source);
            Terms terms = response.getAggregations().get("services");
            List<Map<String, Object>> services = new ArrayList<>();

            for (Terms.Bucket bucket : terms.getBuckets()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("service", bucket.getKeyAsString());
                row.put("logCount", bucket.getDocCount());
                services.add(row);
            }
            String result = objectMapper.writeValueAsString(services);
            log.debug("MCP Tool: listServices found {} services", services.size());
            return result;
        } catch (Exception e) {
            log.error("MCP Tool: listServices failed: {}", e.getMessage(), e);
            return e.getMessage();
        }
    }

    @McpTool(name = "opensearch_top_failing_services", description = "Top failing services")
    public String topFailingServices() throws Exception {
        log.info("MCP Tool: topFailingServices called");
        try {
            SearchSourceBuilder source = new SearchSourceBuilder()
                    .query(QueryBuilders.matchQuery("level", "ERROR"))
                    .aggregation(
                            AggregationBuilders
                                    .terms("services")
                                    .field("service.keyword")
                                    .size(20))
                    .size(0);

            log.debug("Executing OpenSearch aggregation: {}", source);
            SearchResponse response = service.aggregate(source);
            Terms terms = response.getAggregations().get("services");
            List<Map<String, Object>> services = new ArrayList<>();

            for (Terms.Bucket bucket : terms.getBuckets()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("service", bucket.getKeyAsString());
                row.put("errorCount", bucket.getDocCount());
                services.add(row);
            }
            String result = objectMapper.writeValueAsString(services);
            log.debug("MCP Tool: topFailingServices found {} services", services.size());
            return result;
        } catch (Exception e) {
            log.error("MCP Tool: topFailingServices failed: {}", e.getMessage(), e);
            return e.getMessage();
        }
    }

    @McpTool(name = "opensearch_timeout_analysis", description = "Analyze timeouts")
    public String timeoutAnalysis() throws Exception {
        log.info("MCP Tool: timeoutAnalysis called");
        try {
            SearchSourceBuilder source = new SearchSourceBuilder()
                    .query(
                            QueryBuilders.queryStringQuery(
                                    "\"timeout\" OR " +
                                            "\"SocketTimeoutException\" OR " +
                                            "\"ReadTimeoutException\" OR " +
                                            "\"ConnectTimeoutException\""))
                    .size(100);

            log.debug("Executing OpenSearch query: {}", source);
            String result = buildSearchResponse(service.search(source));
            log.debug("MCP Tool: timeoutAnalysis result size: {}", result.length());
            return result;
        } catch (Exception e) {
            log.error("MCP Tool: timeoutAnalysis failed: {}", e.getMessage(), e);
            return e.getMessage();
        }
    }

    @McpTool(name = "opensearch_error_spike_analysis", description = "Analyze error spikes")
    public String errorSpikeAnalysis() throws Exception {
        log.info("MCP Tool: errorSpikeAnalysis called");
        try {
            SearchSourceBuilder source = new SearchSourceBuilder()
                    .query(QueryBuilders.matchQuery("level", "ERROR"))
                    .aggregation(
                            AggregationBuilders
                                    .dateHistogram("errors_over_time")
                                    .field("@timestamp")
                                    .fixedInterval(
                                            DateHistogramInterval.HOUR))
                    .size(0);

            log.debug("Executing OpenSearch aggregation: {}", source);
            SearchResponse response = service.aggregate(source);
            ParsedDateHistogram histogram = response.getAggregations().get("errors_over_time");
            List<Map<String, Object>> buckets = new ArrayList<>();

            histogram.getBuckets().forEach(bucket -> {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("timestamp", bucket.getKeyAsString());
                row.put("errors", bucket.getDocCount());
                buckets.add(row);
            });
            String result = objectMapper.writeValueAsString(buckets);
            log.debug("MCP Tool: errorSpikeAnalysis found {} buckets", buckets.size());
            return result;
        } catch (Exception e) {
            log.error("MCP Tool: errorSpikeAnalysis failed: {}", e.getMessage(), e);
            return e.getMessage();
        }
    }

    private String buildSearchResponse(
            SearchResponse response) throws Exception {
        log.debug("Building search response for {} hits", response.getHits().getTotalHits().value);

        Map<String, Object> result = new LinkedHashMap<>();

        result.put(
                "totalHits",
                response.getHits()
                        .getTotalHits().value);

        List<Map<String, Object>> logs = new ArrayList<>();

        for (SearchHit hit : response.getHits()) {
            logs.add(hit.getSourceAsMap());
        }

        result.put("logs", logs);

        return objectMapper.writeValueAsString(result);
    }
}