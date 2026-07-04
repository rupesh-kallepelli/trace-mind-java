package com.hcltech.orchestrator_agent.response;

import java.util.List;

public record LogQueryResponse(
          Summary summary,
          String topExceptions,
          String rootCause,
          String impact,
          List<String> recommendedActions,
          List<Evidence> evidences) {}
