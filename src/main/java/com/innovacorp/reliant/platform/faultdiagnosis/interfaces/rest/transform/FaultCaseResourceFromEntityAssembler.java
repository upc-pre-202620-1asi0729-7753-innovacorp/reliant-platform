package com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.transform;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultCase;
import com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.resources.FaultCaseResource;

public class FaultCaseResourceFromEntityAssembler {
    public static FaultCaseResource toResourceFromEntity(FaultCase c) {
        return new FaultCaseResource(c.getId(), c.getOrganizationIdValue(), c.getSpraySessionIdValue(), c.getHvofSystemIdValue(), c.getParameter(),
                c.getBand().name().toLowerCase(), c.getValue(), c.getEpochMillis(), c.getProbableCause(), c.getRecommendedAction(),
                c.getStatus().name().toLowerCase(), c.getAcknowledgedByValue(), c.getAcknowledgedAt(), c.getResolvedByValue(), c.getResolvedAt(), c.getResolutionNotes());
    }
}
