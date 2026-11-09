package com.innovacorp.reliant.platform.traceability.interfaces.rest.transform;

import com.innovacorp.reliant.platform.traceability.domain.model.commands.RegisterComponentCommand;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.RegisterComponentResource;

public class RegisterComponentCommandFromResourceAssembler {

    public static RegisterComponentCommand toCommandFromResource(RegisterComponentResource resource) {
        return new RegisterComponentCommand(
                resource.customerId(), resource.serialNumber(), resource.partNumber(),
                resource.componentType(), resource.machineManufacturer(), resource.machineModel(),
                resource.pcrTargetHours());
    }
}