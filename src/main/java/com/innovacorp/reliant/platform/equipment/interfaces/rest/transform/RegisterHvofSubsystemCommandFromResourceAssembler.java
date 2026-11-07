package com.innovacorp.reliant.platform.equipment.interfaces.rest.transform;

import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterHvofSubsystemCommand;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.RegisterHvofSubsystemResource;

import java.util.List;

public class RegisterHvofSubsystemCommandFromResourceAssembler {
    public static RegisterHvofSubsystemCommand toCommandFromResource(Long hvofSystemId, RegisterHvofSubsystemResource resource) {
        var parameters = resource.parameters() == null
                ? List.<String>of()
                : resource.parameters().stream().map(p -> p.parameter()).toList();

        return new RegisterHvofSubsystemCommand(
                hvofSystemId,
                resource.subsystemType(),
                resource.name(),
                resource.alias(),
                parameters
        );
    }
}