package com.innovacorp.reliant.platform.equipment.application.internal.queryservices;

import com.innovacorp.reliant.platform.equipment.application.queryservices.HvofSystemQueryService;
import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetAllHvofSystemsByOrganizationIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetHvofSystemByIdQuery;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.repositories.HvofSystemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class HvofSystemQueryServiceImpl implements HvofSystemQueryService {
    private final HvofSystemRepository hvofSystemRepository;

    public HvofSystemQueryServiceImpl(HvofSystemRepository hvofSystemRepository) {
        this.hvofSystemRepository = hvofSystemRepository;
    }

    @Override
    public Optional<HvofSystem> handle(GetHvofSystemByIdQuery query) {
        log.debug("Querying HVOF system by id={}", query.id());
        return hvofSystemRepository.findById(query.id());
    }

    @Override
    public List<HvofSystem> handle(GetAllHvofSystemsByOrganizationIdQuery query) {
        log.debug("Querying HVOF systems for organizationId={}", query.organizationId().organizationId());
        return hvofSystemRepository.findAllByOrganizationId(query.organizationId());
    }
}