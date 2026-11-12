package com.innovacorp.reliant.platform.traceability.application.acl;

import com.innovacorp.reliant.platform.traceability.application.commandservices.RecuperationCommandService;
import com.innovacorp.reliant.platform.traceability.application.queryservices.RecuperationQueryService;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.LinkSessionToRecuperationCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetRecuperationByIdQuery;
import com.innovacorp.reliant.platform.traceability.interfaces.acl.TraceabilityContextFacade;
import org.springframework.stereotype.Service;

@Service
public class TraceabilityContextFacadeImpl implements TraceabilityContextFacade {
    private final RecuperationQueryService recuperationQueryService;
    private final RecuperationCommandService recuperationCommandService;

    public TraceabilityContextFacadeImpl(RecuperationQueryService recuperationQueryService, RecuperationCommandService recuperationCommandService) {
        this.recuperationQueryService = recuperationQueryService;
        this.recuperationCommandService = recuperationCommandService;
    }

    @Override
    public Long fetchRecuperationSupplierOrganizationId(Long recuperationId) {
        return recuperationQueryService.handle(new GetRecuperationByIdQuery(recuperationId))
                .map(Recuperation::getSupplierOrganizationIdValue)
                .orElse(0L);
    }

    @Override
    public boolean linkSessionToRecuperation(Long recuperationId, Long sessionId) {
        return recuperationCommandService.handle(new LinkSessionToRecuperationCommand(recuperationId, sessionId)).isSuccess();
    }
}
