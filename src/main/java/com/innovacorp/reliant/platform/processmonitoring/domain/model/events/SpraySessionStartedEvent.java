package com.innovacorp.reliant.platform.processmonitoring.domain.model.events;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates.SpraySession;

public record SpraySessionStartedEvent(
        Long spraySessionId,
        Long organizationId,
        Long hvofSystemId,
        Long recuperationId,
        Integer recipeNumber
) {
    public static SpraySessionStartedEvent from(SpraySession session) {
        return new SpraySessionStartedEvent(
                session.getId(), session.getOrganizationIdValue(), session.getHvofSystemIdValue(),
                session.getRecuperationIdValue(), session.getRecipeNumber());
    }
}