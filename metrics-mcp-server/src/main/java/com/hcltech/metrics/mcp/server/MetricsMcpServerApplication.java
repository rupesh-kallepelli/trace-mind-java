package com.hcltech.metrics.mcp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class MetricsMcpServerApplication {

	public static void main(String[] args) {
		log.info("Starting Metrics MCP Server...");
		SpringApplication.run(MetricsMcpServerApplication.class, args);
		log.info("Metrics MCP Server is up and running.");
	}

}
