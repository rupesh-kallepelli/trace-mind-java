package com.hcltech.orchestrator_agent.response;

import lombok.Data;

import java.util.List;

@Data
public class LogInvestigationResponse {

    private String issue;

    private String summary;

    private List<String> affectedServices;

    private List<String> topExceptions;

    private List<String> suspectedCorrelationIds;

    private String evidence;

    private String recommendation;
}