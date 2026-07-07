package com.hcltech.trace.mind.agent.response;

import java.util.List;

import com.google.auto.value.AutoValue.Builder;

import lombok.Data;
@Builder
@Data
public class InvestigationDetailResponse {

    private InvestigationResponse investigation;

    private List<EvidenceResponse> evidence;

    private IncidentResponse incident;
}