package com.innovacorp.reliant.platform.processmonitoring.domain.model.events;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates.SpraySession;

public record SpraySessionCompletedEvent(Long spraySessionId, Long organizationId, Long recuperationId, String status) {
    public static SpraySessionCompletedEvent from(SpraySession s) {
        return new SpraySessionCompletedEvent(s.getId(), s.getOrganizationIdValue(), s.getRecuperationIdValue(), s.getStatus().name());
    }
}
