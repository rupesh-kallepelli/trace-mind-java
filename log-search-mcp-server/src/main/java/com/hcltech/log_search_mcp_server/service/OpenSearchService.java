package com.hcltech.log_search_mcp_server.service;

import lombok.RequiredArgsConstructor;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OpenSearchService {

    private final RestHighLevelClient client;

    public SearchResponse search(SearchSourceBuilder sourceBuilder) throws IOException {

        SearchRequest request = new SearchRequest("logs-*");

        request.source(sourceBuilder);

        return client.search(request, RequestOptions.DEFAULT);
    }

    public long count(String query) throws Exception {

        SearchSourceBuilder source = new SearchSourceBuilder()
                .query(
                        QueryBuilders.queryStringQuery(query))
                .size(0);

        SearchResponse response = search(source);

        return response.getHits().getTotalHits().value;
    }

}