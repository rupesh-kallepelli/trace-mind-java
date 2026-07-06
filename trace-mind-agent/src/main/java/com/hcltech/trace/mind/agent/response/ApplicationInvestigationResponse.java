package com.hcltech.trace.mind.agent.response;

import lombok.Data;

@Data
public class ApplicationInvestigationResponse {

    private String issue;

    private String namespace;

    private String summary;

    private String rootCause;

    private String affectedService;

    private String affectedComponent;

    private String evidence;

    private String recommendation;

    private String preventiveAction;

    private Integer confidenceScore;
}