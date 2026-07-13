package com.hcltech.trace.mind.agent.response.app;

import com.hcltech.trace.mind.agent.entities.app.ApiDefinition;
import com.hcltech.trace.mind.agent.entities.app.Application;
import com.hcltech.trace.mind.agent.entities.app.ApplicationKnowledge;
import com.hcltech.trace.mind.agent.entities.app.DatabaseInfo;
import com.hcltech.trace.mind.agent.entities.app.DatabaseSchema;
import com.hcltech.trace.mind.agent.entities.app.DatabaseTable;
import com.hcltech.trace.mind.agent.entities.app.InfrastructureComponent;
import com.hcltech.trace.mind.agent.entities.app.Microservice;
import com.hcltech.trace.mind.agent.entities.app.ServiceDependency;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationContextResponse {

    private Application application;

    private List<Microservice> microservices;

    private List<ServiceDependency> dependencies;

    private List<DatabaseInfo> databases;

    private List<DatabaseSchema> schemas;

    private List<DatabaseTable> tables;

    private List<ApiDefinition> apis;

    private List<InfrastructureComponent> infrastructureComponents;

    private List<ApplicationKnowledge> knowledge;
}