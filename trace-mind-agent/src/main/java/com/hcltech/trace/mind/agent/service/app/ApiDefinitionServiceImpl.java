package com.hcltech.trace.mind.agent.service.app;

import com.hcltech.trace.mind.agent.entities.app.ApiDefinition;
import com.hcltech.trace.mind.agent.repository.app.ApiDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiDefinitionServiceImpl implements ApiDefinitionService {

    private final ApiDefinitionRepository repository;

    @Override
    public ApiDefinition create(ApiDefinition api) {
        return repository.save(api);
    }

    @Override
    public ApiDefinition update(Long id, ApiDefinition api) {
        ApiDefinition existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("API Definition not found"));
        existing.setName(api.getName());
        existing.setMethod(api.getMethod());
        existing.setPath(api.getPath());
        existing.setDescription(api.getDescription());
        existing.setRequestContext(api.getRequestContext());
        existing.setResponseContext(api.getResponseContext());
        existing.setFailurePatterns(api.getFailurePatterns());
        return repository.save(existing);
    }

    @Override
    public ApiDefinition getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("API Definition not found"));
    }

    @Override
    public List<ApiDefinition> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<ApiDefinition> getByMicroservice(
            Long microserviceId) {

        return repository.findByMicroserviceId(
                microserviceId);
    }

}