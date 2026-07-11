package com.hcltech.trace.mind.agent.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcltech.trace.mind.agent.entities.Investigation;

@Repository
public interface InvestigationRepository
        extends JpaRepository<Investigation, String> {

    List<Investigation> findByServiceName(String serviceName);

    List<Investigation> findByStatus(String status);

    Page<Investigation> findAll(Pageable pageable);
}
