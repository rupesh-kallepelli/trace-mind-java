package com.hcltech.trace.mind.agent.service.app;

import com.hcltech.trace.mind.agent.entities.app.Application;
import com.hcltech.trace.mind.agent.repository.app.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository repository;

    @Override
    public Application create(Application application) {
        return repository.save(application);
    }

    @Override
    public Application update(Long id, Application application) {

        Application existing = getById(id);

        BeanUtils.copyProperties(
                application,
                existing,
                "id",
                "createdAt"
        );

        return repository.save(existing);
    }

    @Override
    public Application getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Application not found"));
    }

    @Override
    public List<Application> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}