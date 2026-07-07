package com.hcltech.metrics.mcp.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@Slf4j
public class MetricsMcpServerApplication {

	public static void main(String[] args) {
		log.info("Starting Metrics MCP Server...");
		SpringApplication.run(MetricsMcpServerApplication.class, args);
		log.info("Metrics MCP Server is up and running.");
	}

	@Bean
	public ObjectMapper objectMapper() {
		log.debug("Creating ObjectMapper bean in main application class");
		return new ObjectMapper();
	}
}
