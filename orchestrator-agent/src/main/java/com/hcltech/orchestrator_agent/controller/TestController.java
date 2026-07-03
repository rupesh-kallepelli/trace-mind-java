package com.hcltech.orchestrator_agent.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TestController {
    private final ChatModel chatModel;
    // private final ChatClient chatClient;

    private final ToolCallbackProvider toolCallbackProvider;

    @GetMapping("/test")
    public String test() {

        Prompt prompt = new Prompt(
                "List files in the trace-mind-java/log-search-mcp-server folder",
                ToolCallingChatOptions.builder()
                        .toolCallbacks(toolCallbackProvider.getToolCallbacks())
                        .build());

        
        return chatModel.call(prompt).getResults().get(0).getOutput().getText();
    }
     @GetMapping("/read-test")
    public String readTest() {

        Prompt prompt = new Prompt(
                "read file trace-mind-java/log-search-mcp-server/src/main/java/com/hcltech/log_search_mcp_server/tools/FileTools.java",
                ToolCallingChatOptions.builder()
                        .toolCallbacks(toolCallbackProvider.getToolCallbacks())
                        .build());

        
        return chatModel.call(prompt).getResults().get(0).getOutput().getText();
    }
}
