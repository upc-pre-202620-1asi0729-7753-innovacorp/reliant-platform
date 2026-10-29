package com.innovacorp.reliant.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Request resource used to register an HVOF system")
public record RegisterHvofSystemResource(
        @Schema(description = "Owning organization id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "{shared.error.organizationId.invalid}") @Positive(message = "{shared.error.organizationId.invalid}")
        Long organizationId,
        @Schema(description = "Unique code within the organization", example = "HVOF-01", maxLength = 32, pattern = "^[A-Z0-9][A-Z0-9-]*$", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{hvof.system.error.code.notBlank}") @Size(max = 32, message = "{hvof.system.error.code.size}")
        @Pattern(regexp = "^[A-Z0-9][A-Z0-9-]*$", message = "{hvof.system.error.code.pattern}")
        String code,
        @Schema(description = "Serial number", example = "DJ2700-PE-0142", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{hvof.system.error.serialNumber.notBlank}") @Size(max = 64)
        String serialNumber,
        @Schema(description = "Manufacturer", example = "Oerlikon Metco", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{hvof.system.error.manufacturer.notBlank}") @Size(max = 80)
        String systemManufacturer,
        @Schema(description = "Model", example = "MultiCoat / Diamond Jet 2700", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{hvof.system.error.model.notBlank}") @Size(max = 120)
        String systemModel,
        @Schema(description = "Fuel type", example = "HYDROGEN", allowableValues = {"HYDROGEN", "KEROSENE", "PROPANE", "NATURAL_GAS"}, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "{hvof.system.error.fuelType.invalid}")
        String fuelType) { }