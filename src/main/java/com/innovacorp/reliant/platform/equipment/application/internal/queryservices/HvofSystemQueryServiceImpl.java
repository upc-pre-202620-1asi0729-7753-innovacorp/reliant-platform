package com.innovacorp.reliant.platform.equipment.application.internal.queryservices;

import com.innovacorp.reliant.platform.equipment.application.queryservices.HvofSystemQueryService;
import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.entities.Controller;
import com.innovacorp.reliant.platform.equipment.domain.model.entities.HvofSubsystem;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetAllControllersByHvofSystemIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetAllHvofSubsystemsByHvofSystemIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetAllHvofSystemsByOrganizationIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetHvofSystemByIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.repositories.HvofSystemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HvofSystemQueryServiceImpl implements HvofSystemQueryService {
    private final HvofSystemRepository hvofSystemRepository;

    public HvofSystemQueryServiceImpl(HvofSystemRepository hvofSystemRepository) {
        this.hvofSystemRepository = hvofSystemRepository;
    }

    @Override
    public Optional<HvofSystem> handle(GetHvofSystemByIdQuery query) {
        return hvofSystemRepository.findById(query.hvofSystemId());
    }

    @Override
    public List<HvofSystem> handle(GetAllHvofSystemsByOrganizationIdQuery query) {
        return hvofSystemRepository.findAllByOrganizationId(query.organizationId());
    }

    @Override
    public List<HvofSubsystem> handle(GetAllHvofSubsystemsByHvofSystemIdQuery query) {
        return hvofSystemRepository.findById(query.hvofSystemId())
                .map(HvofSystem::getSubsystems)
                .orElse(List.of());
    }

    @Override
    public List<Controller> handle(GetAllControllersByHvofSystemIdQuery query) {
        return hvofSystemRepository.findById(query.hvofSystemId())
                .map(HvofSystem::getControllers)
                .orElse(List.of());
    }
}