package com.innovacorp.reliant.platform.equipment.application.commandservices;

import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterHvofSystemCommand;

public interface HvofSystemCommandService {
    Result<HvofSystem, HvofSystemCommandFailure> handle(RegisterHvofSystemCommand command);
}