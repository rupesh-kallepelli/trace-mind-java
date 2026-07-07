package com.hcltech.trace.mind.agent.service;

import java.util.List;

import org.springaicommunity.mcp.annotation.McpToolListChanged;
import org.springframework.stereotype.Service;

import io.modelcontextprotocol.spec.McpSchema;

@Service
public class ApplicationAgentToolsService {


    @McpToolListChanged(clients = "server1")
    public void handleToolListChanged(List<McpSchema.Tool> updatedTools) {
        System.out.println("Tool list updated: " + updatedTools.size() + " tools available");
    }
}
