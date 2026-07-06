package com.hcltech.orchestrator_agent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.modelcontextprotocol.client.transport.customizer.McpSyncHttpClientRequestCustomizer;

@Configuration
public class AppConfig {

    @Bean
    public McpSyncHttpClientRequestCustomizer githubCustomizer() {

        return (builder, method, endpoint, body, context) -> {

            String url = endpoint.toString();

            if (url.contains("github")) {

                builder.header(
                        "Authorization",
                        "Bearer "
                                + "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"
                // + System.getenv("GITHUB_TOKEN")
                );
            }
        };
    }

}
