package com.innovacorp.reliant.platform.equipment.interfaces.rest.transform;

import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterHvofSystemCommand;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.RegisterHvofSystemResource;

/**
 * Assembler to convert a RegisterHvofSystemResource to a RegisterHvofSystemCommand.
 */
public class RegisterHvofSystemCommandFromResourceAssembler {
    public static RegisterHvofSystemCommand toCommandFromResource(RegisterHvofSystemResource resource) {
        return new RegisterHvofSystemCommand(
                resource.organizationId(),
                resource.code(),
                resource.serialNumber(),
                resource.systemManufacturer(),
                resource.systemModel(),
                resource.fuelType());
    }
}