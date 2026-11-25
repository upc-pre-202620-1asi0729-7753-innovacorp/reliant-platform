package com.innovacorp.reliant.platform.traceability.application.acl;

import com.innovacorp.reliant.platform.traceability.application.queryservices.RecuperationQueryService;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetRecuperationByIdQuery;
import com.innovacorp.reliant.platform.traceability.interfaces.acl.TraceabilityContextFacade;
import org.springframework.stereotype.Service;

/**
 * Application-layer implementation of the Traceability ACL facade.
 */
@Service
public class TraceabilityContextFacadeImpl implements TraceabilityContextFacade {
    private final RecuperationQueryService recuperationQueryService;

    public TraceabilityContextFacadeImpl(RecuperationQueryService recuperationQueryService) {
        this.recuperationQueryService = recuperationQueryService;
    }

    @Override
    public Long fetchRecuperationSupplierOrganizationId(Long recuperationId) {
        return recuperationQueryService.handle(new GetRecuperationByIdQuery(recuperationId))
                .map(Recuperation::getSupplierOrganizationIdValue)
                .orElse(0L);
    }
}