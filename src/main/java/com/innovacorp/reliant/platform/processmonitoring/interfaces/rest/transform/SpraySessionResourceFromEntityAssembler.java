package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.transform;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates.SpraySession;
import com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources.SpraySessionResource;

import java.util.Locale;

public class SpraySessionResourceFromEntityAssembler {

    public static SpraySessionResource toResourceFromEntity(SpraySession session) {
        return new SpraySessionResource(
                session.getId(), session.getHvofSystemIdValue(), session.getRecuperationIdValue(),
                session.getOperatorId(), session.getRecipeNumber(), session.getStartedAt(),
                session.getEndedAt(), session.getTimeZone(),
                session.getStatus().name().toLowerCase(Locale.ROOT), session.getAbortReason());
    }
}