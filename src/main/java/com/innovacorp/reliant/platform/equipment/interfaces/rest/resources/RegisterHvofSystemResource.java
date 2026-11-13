package com.innovacorp.reliant.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/**
 * Resource for registering an HVOF system.
 */
@Schema(
        name = "RegisterHvofSystemRequest",
        description = "Request payload for registering an HVOF system",
        example = "{\"organizationId\": 1, \"code\": \"HVOF-01\", \"serialNumber\": \"JP5000-2019-0421\", \"systemManufacturer\": \"Praxair\", \"systemModel\": \"JP-5000\", \"fuelType\": \"KEROSENE\"}"
)
public record RegisterHvofSystemResource(
        @NotNull(message = "{validation.not-blank}")
        @Positive(message = "{validation.positive}")
        @Schema(description = "Owning organization id", example = "1")
        Long organizationId,

        @NotBlank(message = "{validation.not-blank}")
        @Pattern(regexp = "^[A-Z0-9-]{3,20}$", message = "{validation.pattern}")
        @Schema(description = "System code, unique per organization", example = "HVOF-01")
        String code,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Serial number", example = "JP5000-2019-0421")
        String serialNumber,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Manufacturer", example = "Praxair")
        String systemManufacturer,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Model", example = "JP-5000")
        String systemModel,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Fuel type", example = "KEROSENE", allowableValues = {"HYDROGEN", "KEROSENE", "NATURAL_GAS", "PROPANE"})
        String fuelType
) {
}