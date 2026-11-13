package com.innovacorp.reliant.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource for an HVOF system.
 */
@Schema(
        name = "HvofSystemResponse",
        description = "HVOF system information response",
        example = "{\"id\": 1, \"code\": \"HVOF-01\", \"organizationId\": 1, \"serialNumber\": \"JP5000-2019-0421\", \"status\": \"ACTIVE\", \"systemManufacturer\": \"Praxair\", \"systemModel\": \"JP-5000\", \"fuelType\": \"KEROSENE\"}"
)
public record HvofSystemResource(
        @Schema(description = "HVOF system unique identifier", example = "1") Long id,
        @Schema(description = "System code", example = "HVOF-01") String code,
        @Schema(description = "Owning organization id", example = "1") Long organizationId,
        @Schema(description = "Serial number", example = "JP5000-2019-0421") String serialNumber,
        @Schema(description = "Operational status", example = "ACTIVE") String status,
        @Schema(description = "Manufacturer", example = "Praxair") String systemManufacturer,
        @Schema(description = "Model", example = "JP-5000") String systemModel,
        @Schema(description = "Fuel type", example = "KEROSENE") String fuelType
) {
}