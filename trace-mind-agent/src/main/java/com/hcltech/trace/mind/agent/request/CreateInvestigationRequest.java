package com.hcltech.trace.mind.agent.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateInvestigationRequest {

    @NotBlank
    private String issueDescription;

    @NotBlank
    private String serviceName;

    @NotBlank
    private String namespace;

    @NotBlank
    private String createdBy;
}