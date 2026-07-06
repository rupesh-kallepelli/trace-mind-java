package com.hcltech.orchestrator_agent.response;

import lombok.Data;

import java.util.List;

@Data
public class SourceCodeInvestigationResponse {

    private String issue;

    private String summary;

    private List<String> impactedRepositories;

    private List<String> impactedFiles;

    private List<String> impactedClasses;

    private List<String> impactedMethods;

    private List<String> recentCommits;

    private String probableRootCause;

    private String evidence;

    private String recommendation;
}