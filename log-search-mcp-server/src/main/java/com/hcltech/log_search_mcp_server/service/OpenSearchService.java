package com.hcltech.log_search_mcp_server.service;


import org.opensearch.action.search.SearchResponse;
import org.opensearch.search.builder.SearchSourceBuilder;

public interface OpenSearchService {

    SearchResponse search(SearchSourceBuilder source) throws Exception;

    long count(String query) throws Exception;

    SearchResponse aggregate(SearchSourceBuilder source) throws Exception;
}