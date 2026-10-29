package com.innovacorp.reliant.platform.equipment.interfaces.rest.transform;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.HvofSystemResource;

public class HvofSystemResourceFromEntityAssembler {
    public static HvofSystemResource toResourceFromEntity(HvofSystem entity) {
        return new HvofSystemResource(entity.getId(), entity.getOrganizationIdValue(), entity.getCodeValue(), entity.getSerialNumber(),
                entity.getSystemManufacturer(), entity.getSystemModel(), entity.getFuelType().name(), entity.getStatus().name());
    }
}