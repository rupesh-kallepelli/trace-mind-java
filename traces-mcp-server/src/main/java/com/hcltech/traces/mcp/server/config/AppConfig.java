package com.hcltech.traces.mcp.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    public RestClient restClient() {
        log.info("Initializing RestClient bean for Prometheus communication");
        return RestClient.builder().build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        log.info("Initializing ObjectMapper bean for JSON processing");
        return new ObjectMapper();
    }
}