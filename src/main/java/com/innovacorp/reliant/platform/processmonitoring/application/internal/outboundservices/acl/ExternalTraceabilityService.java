package com.innovacorp.reliant.platform.processmonitoring.application.internal.outboundservices.acl;

import com.innovacorp.reliant.platform.traceability.interfaces.acl.TraceabilityContextFacade;
import org.springframework.stereotype.Service;

@Service
public class ExternalTraceabilityService {
    private final TraceabilityContextFacade traceabilityContextFacade;

    public ExternalTraceabilityService(TraceabilityContextFacade traceabilityContextFacade) {
        this.traceabilityContextFacade = traceabilityContextFacade;
    }

    public Long fetchRecuperationSupplierOrganizationId(Long recuperationId) {
        return traceabilityContextFacade.fetchRecuperationSupplierOrganizationId(recuperationId);
    }

    public boolean linkSessionToRecuperation(Long recuperationId, Long sessionId) {
        return traceabilityContextFacade.linkSessionToRecuperation(recuperationId, sessionId);
    }
}
