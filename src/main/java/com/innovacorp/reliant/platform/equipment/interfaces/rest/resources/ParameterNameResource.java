package com.innovacorp.reliant.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "ParameterName", example = "{\"parameter\": \"fuel_gas_flow\"}")
public record ParameterNameResource(
        @NotBlank(message = "{validation.not-blank}")
        @Pattern(regexp = "^[a-z][a-z0-9_]{2,39}$", message = "{validation.pattern}")
        @Schema(example = "fuel_gas_flow") String parameter
) {
}