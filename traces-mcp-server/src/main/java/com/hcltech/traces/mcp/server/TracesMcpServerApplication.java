package com.hcltech.traces.mcp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class TracesMcpServerApplication {

	public static void main(String[] args) {
		log.info("Starting Tra MCP Server...");
		SpringApplication.run(TracesMcpServerApplication.class, args);
		log.info("Traces MCP Server is up and running.");
	}

}
