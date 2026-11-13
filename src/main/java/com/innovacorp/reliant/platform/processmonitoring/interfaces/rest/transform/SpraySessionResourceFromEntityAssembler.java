package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.transform;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates.SpraySession;
import com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources.SpraySessionResource;

public class SpraySessionResourceFromEntityAssembler {
    public static SpraySessionResource toResourceFromEntity(SpraySession s) {
        return new SpraySessionResource(s.getId(), s.getHvofSystemIdValue(), s.getRecuperationIdValue(), s.getOperatorId(), s.getRecipeNumber(),
                s.getStartedAt(), s.getEndedAt(), s.getTimeZone(), s.getStatus().name().toLowerCase(), s.getAbortReason());
    }
}
