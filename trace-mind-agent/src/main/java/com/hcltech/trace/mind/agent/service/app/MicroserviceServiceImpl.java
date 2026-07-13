package com.hcltech.trace.mind.agent.service.app;

import com.hcltech.trace.mind.agent.entities.app.Microservice;
import com.hcltech.trace.mind.agent.repository.app.MicroserviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MicroserviceServiceImpl
        implements MicroserviceService {

    private final MicroserviceRepository repository;

    @Override
    public Microservice create(Microservice microservice) {
        return repository.save(microservice);
    }

    @Override
    public Microservice update(Long id,
                               Microservice microservice) {

        Microservice existing = getById(id);

        BeanUtils.copyProperties(
                microservice,
                existing,
                "id"
        );

        return repository.save(existing);
    }

    @Override
    public Microservice getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Microservice not found"));
    }

    @Override
    public List<Microservice> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Microservice> getByApplication(
            Long applicationId) {

        return repository
                .findByApplicationId(applicationId);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}