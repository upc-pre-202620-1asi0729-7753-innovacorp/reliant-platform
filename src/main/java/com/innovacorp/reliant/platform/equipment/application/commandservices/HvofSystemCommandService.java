package com.innovacorp.reliant.platform.equipment.application.commandservices;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterHvofSystemCommand;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;

/**
 * HVOF System Command Service
 */
public interface HvofSystemCommandService {
    Result<HvofSystem, ApplicationError> handle(RegisterHvofSystemCommand command);
}