package com.hcltech.log_search_mcp_server.response;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogAnalysisResponse {

    private long totalHits;

    private List<Map<String,Object>> logs;
}