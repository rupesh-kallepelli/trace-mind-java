package com.hcltech.orchestrator_agent.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hcltech.orchestrator_agent.response.LogQueryResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TestController {

      private final ChatModel chatModel;
      private final ToolCallbackProvider toolCallbackProvider;

      private static final String SYSTEM_PROMPT = """
                  You are ObservaAI, an expert observability and SRE agent.

                  Always use available tools to gather evidence before answering.

                  Never guess.
                  Never hallucinate.
                  Never say "I think" or "it seems".

                  If no logs are found, return NO Logs found at the moment:
                  If no tools related to log search are found, return NO Tools found

                  Use only information returned by tools.
                  Keep the format identical for every response.
                  """;
      private static final String GITHUB_SYSTEM_PROMPT = """
                  You are connected to GitHub MCP tools.

                  CRITICAL RULES:

                  1. For ANY GitHub-related question, you MUST call a GitHub tool before answering.

                  2. Never answer from your own knowledge.

                  3. Never say:
                     - "I don't have access"
                     - "I cannot determine"
                     - "I am an AI assistant"

                     until you have first attempted one or more GitHub MCP tool calls.

                  4. Repository information, users, organizations, issues, pull requests,
                  commits, branches, releases, workflows and files MUST come from tool results.

                  5. If a tool returns no data, explicitly state:
                     "No matching GitHub data was returned by the available tools."

                  6. Always include:
                     - Tool(s) used
                     - Repository searched
                     - Evidence returned by tool

                  7. Do not invent repository names, users, commits, or pull requests.

                  Before answering any GitHub question, invoke a GitHub MCP tool.
                  """;

      @GetMapping("/query-logs/{query}")
      public ResponseEntity<LogQueryResponse> queryLogs(@PathVariable String query) {

            BeanOutputConverter<LogQueryResponse> converter = new BeanOutputConverter<>(LogQueryResponse.class);
            ToolCallback[] filtered = Arrays.stream(toolCallbackProvider.getToolCallbacks())
                        .filter(t -> !"issue_write".equals(
                                    t.getToolDefinition().name()))
                        .toArray(ToolCallback[]::new);
            String format = converter.getFormat();
            Prompt prompt = new Prompt(
                        List.of(
                                    new SystemMessage(SYSTEM_PROMPT + "\n\n" + format),
                                    new UserMessage(query)),
                        ToolCallingChatOptions.builder()
                                    .toolCallbacks(filtered)
                                    .build());
            String response = chatModel.call(prompt)
                        .getResult()
                        .getOutput()
                        .getText();

            return ResponseEntity.ok(
                        converter.convert(response));
      }

      @GetMapping("/query-github/{query}")
      public String queryGithub(@PathVariable String query) {

            ToolCallback[] filtered = Arrays.stream(toolCallbackProvider.getToolCallbacks())
                        .filter(t -> !"issue_write".equals(
                                    t.getToolDefinition().name()))
                        .toArray(ToolCallback[]::new);

            Prompt prompt = new Prompt(
                        List.of(
                              // new SystemMessage(GITHUB_SYSTEM_PROMPT),
                                    new UserMessage(query)),
                        ToolCallingChatOptions.builder()
                                    .toolCallbacks(filtered)
                                    .build());

            ChatResponse response = chatModel.call(prompt);

            return response.getResults()
                        .stream()
                        .map(result -> result.getOutput().getText())
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining("\n"));
      }
}