package com.innovacorp.reliant.platform.traceability.domain.model.events;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;

public record RecuperationCompletedEvent(Long recuperationId, Long componentId, Long customerId) {
    public static RecuperationCompletedEvent from(Recuperation r) {
        return new RecuperationCompletedEvent(r.getId(), r.getComponentIdValue(), r.getCustomerIdValue());
    }
}
