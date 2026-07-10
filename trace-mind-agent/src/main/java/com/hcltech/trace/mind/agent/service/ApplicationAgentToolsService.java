package com.hcltech.trace.mind.agent.service;

import java.util.List;

import org.springaicommunity.mcp.annotation.McpToolListChanged;
import org.springframework.stereotype.Service;

import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApplicationAgentToolsService {

    @McpToolListChanged(clients = "server1")
    public void handleToolListChanged(List<McpSchema.Tool> updatedTools) {
        log.info("Tool list updated: {} tools available", updatedTools.size());
        if (log.isDebugEnabled()) {
            updatedTools.forEach(tool -> log.debug("Available tool: {}", tool.name()));
        }
    }
}
