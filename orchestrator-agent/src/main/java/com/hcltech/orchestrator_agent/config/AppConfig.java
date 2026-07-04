package com.hcltech.orchestrator_agent.config;

import java.net.http.HttpRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;

@Configuration
public class AppConfig {
    // @Bean
    // public McpSyncClient githubClient() {

    // HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
    // .header(
    // "Authorization",
    // "Bearer "
    // // + System.getenv("GITHUB_TOKEN")
    // );

    // var transport = HttpClientStreamableHttpTransport.builder(
    // "http://localhost:9601")
    // .endpoint("/mcp")
    // .requestBuilder(requestBuilder)
    // .build();

    // return McpClient.sync(transport).build();
    // }

    @Bean
    public McpSyncClient githubClient() {

        var transport = HttpClientStreamableHttpTransport
                .builder("http://localhost:9601")
                .endpoint("/mcp")
                .httpRequestCustomizer(
                        (builder, method, endpoint, body, context) -> {

                            builder.header(
                                    "Authorization",
                                    "Bearer " 
                                    + "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"
                                    // + System.getenv("GITHUB_TOKEN")
                                );
                        })
                .build();

        var client = McpClient.sync(transport).build();

        client.initialize();

        System.out.println("TOOLS:");
        System.out.println(client.listTools());

        return client;
    }
}
