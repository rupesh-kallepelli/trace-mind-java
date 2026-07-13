package com.hcltech.trace.mind.agent.repository.app;

import com.hcltech.trace.mind.agent.entities.app.DatabaseInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseInfoRepository extends JpaRepository<DatabaseInfo, Long> {
    List<DatabaseInfo> findByApplicationId(Long applicationId);
}