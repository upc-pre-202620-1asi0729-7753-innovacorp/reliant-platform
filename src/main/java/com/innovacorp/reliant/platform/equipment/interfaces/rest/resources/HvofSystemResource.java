package com.innovacorp.reliant.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "HVOF system resource")
public record HvofSystemResource(Long id, Long organizationId, String code, String serialNumber,
                                 String systemManufacturer, String systemModel, String fuelType, String status) { }