package com.innovacorp.reliant.platform.traceability.application.queryservices;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllRecuperationsByCustomerIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllRecuperationsBySupplierOrganizationIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetRecuperationByIdQuery;

import java.util.List;
import java.util.Optional;

public interface RecuperationQueryService {
    Optional<Recuperation> handle(GetRecuperationByIdQuery query);

    List<Recuperation> handle(GetAllRecuperationsBySupplierOrganizationIdQuery query);

    List<Recuperation> handle(GetAllRecuperationsByCustomerIdQuery query);
}
