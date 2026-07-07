
package com.hcltech.trace.mind.agent.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcltech.trace.mind.agent.response.DashboardOverviewResponse;
import com.hcltech.trace.mind.agent.service.DashboardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewResponse> overview() {
        log.info("REST request to get dashboard overview");
        try {
            DashboardOverviewResponse overview = dashboardService.getOverview();
            log.debug("Dashboard overview data retrieved successfully");
            return ResponseEntity.ok(overview);
        } catch (Exception e) {
            log.error("Failed to get dashboard overview: {}", e.getMessage(), e);
            throw e;
        }
    }
}