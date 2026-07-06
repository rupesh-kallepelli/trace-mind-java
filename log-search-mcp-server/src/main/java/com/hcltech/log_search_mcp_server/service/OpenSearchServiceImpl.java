package com.hcltech.log_search_mcp_server.service;

import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenSearchServiceImpl implements OpenSearchService {

    private final RestHighLevelClient client;

    @Value("${opensearch.index}")
    private String indexName;

    @Override
    public SearchResponse search(SearchSourceBuilder source) throws Exception {
        log.info("OpenSearch Service: search called");
        try {
            SearchRequest request = new SearchRequest(indexName);
            request.source(source);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            log.debug("OpenSearch Service: search hits: {}", response.getHits().getTotalHits().value);
            return response;
        } catch (Exception e) {
            log.error("OpenSearch Service: search failed: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public SearchResponse aggregate(SearchSourceBuilder source) throws Exception {
        log.info("OpenSearch Service: aggregate called");
        try {
            SearchRequest request = new SearchRequest(indexName);
            request.source(source);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            log.debug("OpenSearch Service: aggregate returned result");
            return response;
        } catch (Exception e) {
            log.error("OpenSearch Service: aggregate failed: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public long count(String query) throws Exception {
        log.info("OpenSearch Service: count called for query: {}", query);
        try {
            SearchSourceBuilder source =
                    new SearchSourceBuilder()
                            .query(
                                    QueryBuilders.queryStringQuery(
                                            query))
                            .trackTotalHits(true)
                            .size(0);

            SearchResponse response =
                    search(source);

            long count = response.getHits()
                    .getTotalHits()
                    .value;
            log.debug("OpenSearch Service: count result: {}", count);
            return count;
        } catch (Exception e) {
            log.error("OpenSearch Service: count failed for '{}': {}", query, e.getMessage());
            throw e;
        }
    }
}