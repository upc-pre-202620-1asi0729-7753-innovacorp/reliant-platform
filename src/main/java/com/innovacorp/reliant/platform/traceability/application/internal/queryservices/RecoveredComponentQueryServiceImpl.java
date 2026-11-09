package com.innovacorp.reliant.platform.traceability.application.internal.queryservices;

import com.innovacorp.reliant.platform.traceability.application.queryservices.RecoveredComponentQueryService;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.RecoveredComponent;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllComponentsByCustomerIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetComponentByIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.repositories.RecoveredComponentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecoveredComponentQueryServiceImpl implements RecoveredComponentQueryService {

    private final RecoveredComponentRepository componentRepository;

    public RecoveredComponentQueryServiceImpl(RecoveredComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    @Override
    public Optional<RecoveredComponent> handle(GetComponentByIdQuery query) {
        return componentRepository.findById(query.componentId());
    }

    @Override
    public List<RecoveredComponent> handle(GetAllComponentsByCustomerIdQuery query) {
        return componentRepository.findAllByCustomerId(query.customerId());
    }
}