package com.hcltech.orchestrator_agent.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

            If no logs are found, return:

            ==================================================
            SUMMARY
            ==================================================
            No matching logs found.

            For successful searches ALWAYS return EXACTLY this format:

            ==================================================
            SUMMARY
            ==================================================
            Total Logs:
            Error Logs:
            Applications:
            Correlation IDs:

            ==================================================
            TOP EXCEPTIONS
            ==================================================
            1.
            2.
            3.
            4.
            5.

            ==================================================
            ROOT CAUSE
            ==================================================
            <root cause>

            ==================================================
            IMPACT
            ==================================================
            <impact>

            ==================================================
            RECOMMENDED ACTIONS
            ==================================================
            1.
            2.
            3.
            4.
            5.

            ==================================================
            EVIDENCE
            ==================================================
            Timestamp:
            Application:
            CorrelationId:
            Exception:
            Message:

            Use only information returned by tools.
            Keep the format identical for every response.
            """;

    @GetMapping("/query-logs/{query}")
    public String queryLogs(@PathVariable String query) {

        Prompt prompt = new Prompt(
                List.of(
                        new SystemMessage(SYSTEM_PROMPT),
                        new UserMessage(query)),
                ToolCallingChatOptions.builder()
                        .toolCallbacks(toolCallbackProvider.getToolCallbacks())
                        .build());

        ChatResponse response = chatModel.call(prompt);

        return response.getResults()
                .stream()
                .map(result -> result.getOutput().getText())
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));
    }
}