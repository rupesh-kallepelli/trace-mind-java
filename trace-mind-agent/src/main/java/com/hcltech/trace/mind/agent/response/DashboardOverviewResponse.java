package com.hcltech.trace.mind.agent.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardOverviewResponse {

    private Long totalInvestigations;

    private Long runningInvestigations;

    private Long completedInvestigations;

    private Long openIncidents;

    private Long criticalServices;
}