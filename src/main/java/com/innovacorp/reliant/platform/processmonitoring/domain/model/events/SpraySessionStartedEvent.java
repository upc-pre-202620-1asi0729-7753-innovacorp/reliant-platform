package com.innovacorp.reliant.platform.processmonitoring.domain.model.events;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates.SpraySession;

public record SpraySessionStartedEvent(Long spraySessionId, Long organizationId, Long hvofSystemId, Long recuperationId, Integer recipeNumber) {
    public static SpraySessionStartedEvent from(SpraySession s) {
        return new SpraySessionStartedEvent(s.getId(), s.getOrganizationIdValue(), s.getHvofSystemIdValue(), s.getRecuperationIdValue(), s.getRecipeNumber());
    }
}
