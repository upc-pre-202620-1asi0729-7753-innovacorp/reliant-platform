package com.innovacorp.reliant.platform.equipment.application.commandservices;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterControllerCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterHvofSubsystemCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterHvofSystemCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.entities.Controller;
import com.innovacorp.reliant.platform.equipment.domain.model.entities.HvofSubsystem;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;

public interface HvofSystemCommandService {
    Result<HvofSystem, ApplicationError> handle(RegisterHvofSystemCommand command);

    Result<HvofSubsystem, ApplicationError> handle(RegisterHvofSubsystemCommand command);

    Result<Controller, ApplicationError> handle(RegisterControllerCommand command);
}