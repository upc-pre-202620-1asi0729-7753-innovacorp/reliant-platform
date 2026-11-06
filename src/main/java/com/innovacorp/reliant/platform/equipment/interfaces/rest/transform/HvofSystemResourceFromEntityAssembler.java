package com.innovacorp.reliant.platform.equipment.interfaces.rest.transform;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.HvofSystemResource;

/**
 * Assembler to convert an HvofSystem entity to an HvofSystemResource.
 */
public class HvofSystemResourceFromEntityAssembler {
    public static HvofSystemResource toResourceFromEntity(HvofSystem entity) {
        return new HvofSystemResource(
                entity.getId(),
                entity.getCodeValue(),
                entity.getOrganizationIdValue(),
                entity.getSerialNumber(),
                entity.getStatus().name(),
                entity.getSystemManufacturer(),
                entity.getSystemModel(),
                entity.getFuelType().name());
    }
}