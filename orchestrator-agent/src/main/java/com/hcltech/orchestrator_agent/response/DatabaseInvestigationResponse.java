package com.hcltech.orchestrator_agent.response;

import lombok.Data;

import java.util.List;

@Data
public class DatabaseInvestigationResponse {

    private String issue;

    private String summary;

    private List<String> affectedTables;

    private List<String> suspectedConstraints;

    private List<String> suspectedIndexes;

    private String evidence;

    private String probableRootCause;

    private String recommendation;
}