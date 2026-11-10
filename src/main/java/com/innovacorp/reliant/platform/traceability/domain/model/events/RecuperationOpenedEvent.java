package com.innovacorp.reliant.platform.traceability.domain.model.events;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;

public record RecuperationOpenedEvent(
        Long recuperationId, Long componentId, Long customerId, String workOrderNumber) {

    public static RecuperationOpenedEvent from(Recuperation recuperation) {
        return new RecuperationOpenedEvent(
                recuperation.getId(), recuperation.getComponentIdValue(),
                recuperation.getCustomerIdValue(), recuperation.getWorkOrderNumberValue());
    }
}