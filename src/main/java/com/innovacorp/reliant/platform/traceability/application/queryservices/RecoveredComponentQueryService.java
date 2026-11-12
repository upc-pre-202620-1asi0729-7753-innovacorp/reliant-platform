package com.innovacorp.reliant.platform.traceability.application.queryservices;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.RecoveredComponent;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllComponentsByCustomerIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetComponentByIdQuery;

import java.util.List;
import java.util.Optional;

public interface RecoveredComponentQueryService {
    Optional<RecoveredComponent> handle(GetComponentByIdQuery query);

    List<RecoveredComponent> handle(GetAllComponentsByCustomerIdQuery query);
}
