package com.innovacorp.reliant.platform.traceability.domain.model.events;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.RecoveredComponent;

public record ComponentRegisteredEvent(Long componentId, Long customerId, String serialNumber) {
    public static ComponentRegisteredEvent from(RecoveredComponent component) {
        return new ComponentRegisteredEvent(component.getId(), component.getCustomerIdValue(), component.getSerialNumber());
    }
}
