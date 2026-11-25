package com.innovacorp.reliant.platform.processmonitoring.application.internal.outboundservices.acl;

import com.innovacorp.reliant.platform.traceability.interfaces.acl.TraceabilityContextFacade;
import org.springframework.stereotype.Service;

/**
 * ACL service used by Process Monitoring to query Traceability. Linking a session to its recuperation is no longer a
 * call through this service: it is a reaction of Traceability to {@code SpraySessionStartedIntegrationEvent}.
 */
@Service
public class ExternalTraceabilityService {
    private final TraceabilityContextFacade traceabilityContextFacade;

    public ExternalTraceabilityService(TraceabilityContextFacade traceabilityContextFacade) {
        this.traceabilityContextFacade = traceabilityContextFacade;
    }

    public Long fetchRecuperationSupplierOrganizationId(Long recuperationId) {
        return traceabilityContextFacade.fetchRecuperationSupplierOrganizationId(recuperationId);
    }
}