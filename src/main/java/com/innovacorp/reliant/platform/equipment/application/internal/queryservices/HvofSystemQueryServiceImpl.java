package com.innovacorp.reliant.platform.equipment.application.internal.queryservices;

import com.innovacorp.reliant.platform.equipment.application.queryservices.HvofSystemQueryService;
import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetAllHvofSystemsByOrganizationIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetHvofSystemByIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.repositories.HvofSystemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * HVOF System Query Service Implementation
 */
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
}