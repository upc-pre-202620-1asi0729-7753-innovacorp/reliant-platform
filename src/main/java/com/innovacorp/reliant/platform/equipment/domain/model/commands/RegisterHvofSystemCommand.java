package com.innovacorp.reliant.platform.equipment.domain.model.commands;

import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.FuelType;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemCode;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

public record RegisterHvofSystemCommand(
        OrganizationId organizationId,
        HvofSystemCode code,
        String serialNumber,
        String systemManufacturer,
        String systemModel,
        FuelType fuelType) {
    public RegisterHvofSystemCommand {
        if (organizationId == null)
            throw new IllegalArgumentException("shared.error.organizationId.invalid");
        if (code == null)
            throw new IllegalArgumentException("hvof.system.error.code.notBlank");
        if (serialNumber == null || serialNumber.isBlank())
            throw new IllegalArgumentException("hvof.system.error.serialNumber.notBlank");
        if (systemManufacturer == null || systemManufacturer.isBlank())
            throw new IllegalArgumentException("hvof.system.error.manufacturer.notBlank");
        if (systemModel == null || systemModel.isBlank())
            throw new IllegalArgumentException("hvof.system.error.model.notBlank");
        if (fuelType == null)
            throw new IllegalArgumentException("hvof.system.error.fuelType.invalid");
    }
}