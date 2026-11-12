package com.innovacorp.reliant.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(name = "RegisterHvofSubsystemRequest",
        example = "{\"subsystemType\": \"GAS_CONSOLE\", \"name\": \"Gas console\", \"alias\": \"GC1\", \"parameters\": [{\"parameter\": \"fuel_gas_flow\"}, {\"parameter\": \"oxygen_flow\"}]}")
public record RegisterHvofSubsystemResource(
        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "GAS_CONSOLE", allowableValues = {"GAS_CONSOLE", "POWDER_FEEDER", "COOLING_UNIT", "MANIPULATOR", "DUST_COLLECTOR", "SPRAY_GUN"})
        String subsystemType,
        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "Gas console") String name,
        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "GC1") String alias,
        List<@Valid ParameterNameResource> parameters
) {
}