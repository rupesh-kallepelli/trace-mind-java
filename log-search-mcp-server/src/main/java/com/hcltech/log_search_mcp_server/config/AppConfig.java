package com.hcltech.log_search_mcp_server.config;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public RestHighLevelClient openSearchClient() {

        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("opensearch-kallepelli-rupesh-dev.apps.rm1.0a51.p1.openshiftapps.com", 443,
                                "https")));
    }
}