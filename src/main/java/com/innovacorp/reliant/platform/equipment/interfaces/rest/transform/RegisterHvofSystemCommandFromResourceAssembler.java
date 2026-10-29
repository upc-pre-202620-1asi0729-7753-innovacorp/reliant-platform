package com.innovacorp.reliant.platform.equipment.interfaces.rest.transform;

import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterHvofSystemCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.FuelType;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemCode;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.RegisterHvofSystemResource;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

public class RegisterHvofSystemCommandFromResourceAssembler {
    public static RegisterHvofSystemCommand toCommandFromResource(RegisterHvofSystemResource resource) {
        FuelType fuelType;
        try {
            fuelType = FuelType.valueOf(resource.fuelType().trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("hvof.system.error.fuelType.invalid");
        }
        return new RegisterHvofSystemCommand(
                new OrganizationId(resource.organizationId()),
                new HvofSystemCode(resource.code().trim()),
                resource.serialNumber().trim(),
                resource.systemManufacturer().trim(),
                resource.systemModel().trim(),
                fuelType);
    }
}