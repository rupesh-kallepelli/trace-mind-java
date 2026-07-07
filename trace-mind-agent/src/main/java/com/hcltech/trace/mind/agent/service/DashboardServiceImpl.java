package com.hcltech.trace.mind.agent.service;

import org.springframework.stereotype.Service;

import com.hcltech.trace.mind.agent.repository.IncidentRepository;
import com.hcltech.trace.mind.agent.repository.InvestigationRepository;
import com.hcltech.trace.mind.agent.response.DashboardOverviewResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl
        implements DashboardService {

    private final InvestigationRepository investigationRepository;

    private final IncidentRepository incidentRepository;

    @Override
    public DashboardOverviewResponse getOverview() {
        log.info("Generating dashboard overview stats");
        long openIncidents = incidentRepository.countByStatus("OPEN");
        log.debug("Counted {} open incidents", openIncidents);
        
        return DashboardOverviewResponse.builder()
                .totalInvestigations(
                        investigationRepository.count())
                .runningInvestigations(
                        (long) investigationRepository
                                .findByStatus("RUNNING")
                                .size())
                .completedInvestigations(
                        (long) investigationRepository
                                .findByStatus("COMPLETED")
                                .size())
                .openIncidents(
                        openIncidents)
                .criticalServices(0L)
                .build();
    }
}