package com.hcltech.log_search_mcp_server.tools;

import com.hcltech.log_search_mcp_server.service.OpenSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.index.query.BoolQueryBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.aggregations.AggregationBuilders;
import org.opensearch.search.aggregations.bucket.terms.Terms;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.search.sort.SortOrder;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OpenSearchTools {

        private final OpenSearchService service;

        @McpTool(name = "opensearch_search_logs_by_text", description = "Search logs containing the given text")
        public String searchLogsByText(
                        @McpToolParam(description = "Text or query to search") String query) throws Exception {
                log.info("Searching logs by text: {}", query);
                SearchSourceBuilder source = new SearchSourceBuilder()
                                .query(QueryBuilders.queryStringQuery(query))
                                .size(50);

                SearchResponse response = service.search(source);

                StringBuilder builder = new StringBuilder();
                response.getHits().forEach(hit -> {
                        builder.append(hit.getSourceAsString());
                });

                return builder.toString();
        }

        @McpTool(name = "opensearch_search_logs_by_timerange", description = "Search logs within a time range")
        public String searchLogsByTimeRange(
                        @McpToolParam(description = "Start timestamp in ISO8601 format") String startTime,

                        @McpToolParam(description = "End timestamp in ISO8601 format") String endTime)
                        throws Exception {
                log.info("Searching logs by time range: {} to {}", startTime, endTime);
                SearchSourceBuilder source = new SearchSourceBuilder()
                                .query(
                                                QueryBuilders.rangeQuery("@timestamp")
                                                                .gte(startTime)
                                                                .lte(endTime))
                                .size(100);

                SearchResponse response = service.search(source);

                StringBuilder builder = new StringBuilder();
                response.getHits().forEach(hit -> {
                        String sourceStr = hit.getSourceAsString();
                        log.debug("Found log: {}", sourceStr);
                        builder.append(sourceStr).append("\n");
                });

                return builder.toString();
        }

        @McpTool(name = "opensearch_search_logs", description = "Search logs using text and timeframe")
        public String searchLogs(
                        @McpToolParam(description = "Text query") String query,

                        @McpToolParam(description = "Start timestamp in ISO8601 format") String startTime,

                        @McpToolParam(description = "End timestamp in ISO8601 format") String endTime)
                        throws Exception {
                log.info("Searching logs by query '{}' in range {} to {}", query, startTime, endTime);
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                                .must(QueryBuilders.queryStringQuery(query))
                                .filter(
                                                QueryBuilders.rangeQuery("@timestamp")
                                                                .gte(startTime)
                                                                .lte(endTime));

                SearchSourceBuilder source = new SearchSourceBuilder()
                                .query(boolQuery)
                                .size(100);

                SearchResponse response = service.search(source);

                StringBuilder builder = new StringBuilder();
                response.getHits().forEach(hit -> {
                        String sourceStr = hit.getSourceAsString();
                        log.debug("Found log: {}", sourceStr);
                        builder.append(sourceStr).append("\n");
                });

                return builder.toString();
        }

        @McpTool(name = "opensearch_recent_errors", description = "Get recent error logs")
        public String recentErrors() throws Exception {
                log.info("Fetching recent error logs");
                SearchSourceBuilder source = new SearchSourceBuilder()
                                .query(QueryBuilders.matchQuery("level", "ERROR"))
                                .sort("@timestamp", SortOrder.DESC)
                                .size(50);

                SearchResponse response = service.search(source);

                StringBuilder builder = new StringBuilder();

                response.getHits().forEach(hit -> {
                        builder.append(hit.getSourceAsString()).append("\n");
                });

                return builder.toString();
        }

        @McpTool(name = "opensearch_trace_correlation", description = "Trace logs using correlation ID")
        public String traceCorrelation(
                        @McpToolParam(description = "Correlation ID") String correlationId) throws Exception {

                log.info("Tracing correlation ID: {}", correlationId);
                SearchSourceBuilder source = new SearchSourceBuilder()
                                .query(
                                                QueryBuilders.termQuery(
                                                                "correlationId.keyword",
                                                                correlationId))
                                .size(500);

                SearchResponse response = service.search(source);

                return "Printed "
                                + response.getHits().getHits().length
                                + " correlation logs";
        }

        @McpTool(name = "opensearch_top_exceptions", description = "Get most frequent exceptions")
        public String topExceptions() throws Exception {
                log.info("Fetching top exceptions");
                SearchSourceBuilder source = new SearchSourceBuilder()
                                .aggregation(
                                                AggregationBuilders
                                                                .terms("exceptions")
                                                                .field("exception.keyword")
                                                                .size(20))
                                .size(0);

                SearchResponse response = service.search(source);

                Terms terms = response.getAggregations().get("exceptions");

                StringBuilder result = new StringBuilder();

                for (Terms.Bucket bucket : terms.getBuckets()) {

                        String line = bucket.getKeyAsString()
                                        + " => "
                                        + bucket.getDocCount();

                        log.debug("Exception stats: {}", line);

                        result.append(line).append("\n");
                }

                return result.toString();
        }

        @McpTool(name = "opensearch_search_by_field", description = "Search documents using a field name and value")
        public String searchByField(
                        @McpToolParam(description = "Field name") String field,

                        @McpToolParam(description = "Field value") String value) throws Exception {
                log.info("Searching logs by field: {} = {}", field, value);
                SearchSourceBuilder source = new SearchSourceBuilder()
                                .query(QueryBuilders.matchQuery(field, value))
                                .size(100);

                SearchResponse response = service.search(source);

                StringBuilder builder = new StringBuilder();
                response.getHits().forEach(hit -> {
                        builder.append(hit.getSourceAsString()).append("\n");
                });

                return builder.toString();
        }

        @McpTool(name = "opensearch_count_logs", description = "Count number of logs matching a query")
        public String countLogs(@McpToolParam(description = "Search query") String query) throws Exception {
                log.info("Counting logs for query: {}", query);
                long count = service.count(query);

                return "Matching log count: " + count;
        }
}