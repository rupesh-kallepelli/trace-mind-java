package com.hcltech.orchestrator_agent.response;
import java.util.List;

import lombok.Data;

@Data
public class RuntimeInvestigationResponse {

    private String issue;

    private String namespace;

    private String summary;

    private List<String> affectedNamespaces;

    private List<String> affectedDeployments;

    private List<String> affectedPods;

    private List<String> clusterEvents;

    private String podHealth;

    private String resourceHealth;

    private String probableRootCause;

    private String evidence;

    private String recommendation;

    private Integer confidenceScore;
}