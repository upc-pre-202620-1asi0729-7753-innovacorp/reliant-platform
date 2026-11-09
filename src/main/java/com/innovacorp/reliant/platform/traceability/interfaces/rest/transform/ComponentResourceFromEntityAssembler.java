package com.innovacorp.reliant.platform.traceability.interfaces.rest.transform;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.RecoveredComponent;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.ComponentResource;

public class ComponentResourceFromEntityAssembler {

    public static ComponentResource toResourceFromEntity(RecoveredComponent entity) {
        return new ComponentResource(
                entity.getId(), entity.getSerialNumber(), entity.getPartNumber(),
                entity.getComponentType().name(), entity.getMachineManufacturer(),
                entity.getMachineModel(), entity.getCustomerIdValue(), entity.getPcrTargetHours(),
                entity.getStatus().name());
    }
}