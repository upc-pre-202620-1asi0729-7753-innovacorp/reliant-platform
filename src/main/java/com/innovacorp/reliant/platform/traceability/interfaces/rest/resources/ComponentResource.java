package com.innovacorp.reliant.platform.traceability.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ComponentResponse")
public record ComponentResource(
        Long id,
        String serialNumber,
        String partNumber,
        String componentType,
        String machineManufacturer,
        String machineModel,
        Long customerId,
        Integer pcrTargetHours,
        String status
) {
}