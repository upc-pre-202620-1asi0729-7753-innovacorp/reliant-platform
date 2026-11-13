package com.innovacorp.reliant.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "HvofSubsystemResponse")
public record HvofSubsystemResource(
        Long id,
        Long hvofSystemId,
        String subsystemType,
        String name,
        String alias,
        List<ParameterNameResource> parameters
) {
}