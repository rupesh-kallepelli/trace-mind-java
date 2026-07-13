package com.hcltech.trace.mind.agent.service.app;

import com.hcltech.trace.mind.agent.entities.app.ApiDefinition;
import com.hcltech.trace.mind.agent.entities.app.Application;
import com.hcltech.trace.mind.agent.entities.app.ApplicationKnowledge;
import com.hcltech.trace.mind.agent.entities.app.DatabaseInfo;
import com.hcltech.trace.mind.agent.entities.app.DatabaseSchema;
import com.hcltech.trace.mind.agent.entities.app.DatabaseTable;
import com.hcltech.trace.mind.agent.entities.app.InfrastructureComponent;
import com.hcltech.trace.mind.agent.entities.app.Microservice;
import com.hcltech.trace.mind.agent.entities.app.ServiceDependency;
import com.hcltech.trace.mind.agent.repository.app.ApiDefinitionRepository;
import com.hcltech.trace.mind.agent.repository.app.ApplicationKnowledgeRepository;
import com.hcltech.trace.mind.agent.repository.app.ApplicationRepository;
import com.hcltech.trace.mind.agent.repository.app.DatabaseInfoRepository;
import com.hcltech.trace.mind.agent.repository.app.DatabaseSchemaRepository;
import com.hcltech.trace.mind.agent.repository.app.DatabaseTableRepository;
import com.hcltech.trace.mind.agent.repository.app.InfrastructureComponentRepository;
import com.hcltech.trace.mind.agent.repository.app.MicroserviceRepository;
import com.hcltech.trace.mind.agent.repository.app.ServiceDependencyRepository;
import com.hcltech.trace.mind.agent.response.app.ApplicationContextResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationContextServiceImpl
        implements ApplicationContextService {

    private final ApplicationRepository applicationRepository;

    private final MicroserviceRepository microserviceRepository;

    private final ServiceDependencyRepository dependencyRepository;

    private final DatabaseInfoRepository databaseRepository;

    private final DatabaseSchemaRepository schemaRepository;

    private final DatabaseTableRepository tableRepository;

    private final ApiDefinitionRepository apiRepository;

    private final InfrastructureComponentRepository infrastructureRepository;

    private final ApplicationKnowledgeRepository knowledgeRepository;

    @Override
    public ApplicationContextResponse buildContext(
            Long applicationId) {

        Application application =
                applicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException("Application not found"));

        List<Microservice> services =
                microserviceRepository
                        .findByApplicationId(applicationId);

        List<Long> serviceIds =
                services.stream()
                        .map(Microservice::getId)
                        .toList();

        List<ServiceDependency> dependencies =
                dependencyRepository
                        .findBySourceServiceIdIn(serviceIds);

        List<ApiDefinition> apis =
                apiRepository
                        .findByMicroserviceIdIn(serviceIds);

        List<DatabaseInfo> databases =
                databaseRepository
                        .findByApplicationId(applicationId);

        List<Long> databaseIds =
                databases.stream()
                        .map(DatabaseInfo::getId)
                        .toList();

        List<DatabaseSchema> schemas =
                schemaRepository
                        .findByDatabaseIdIn(databaseIds);

        List<Long> schemaIds =
                schemas.stream()
                        .map(DatabaseSchema::getId)
                        .toList();

        List<DatabaseTable> tables =
                tableRepository
                        .findBySchemaIdIn(schemaIds);

        List<InfrastructureComponent> infrastructure =
                infrastructureRepository
                        .findByApplicationId(applicationId);

        List<ApplicationKnowledge> knowledge =
                knowledgeRepository
                        .findByApplicationId(applicationId);

        return ApplicationContextResponse.builder()
                .application(application)
                .microservices(services)
                .dependencies(dependencies)
                .databases(databases)
                .schemas(schemas)
                .tables(tables)
                .apis(apis)
                .infrastructureComponents(infrastructure)
                .knowledge(knowledge)
                .build();
    }
}