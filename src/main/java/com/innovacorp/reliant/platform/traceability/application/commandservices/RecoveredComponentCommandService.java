package com.innovacorp.reliant.platform.traceability.application.commandservices;

import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.RecoveredComponent;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.RegisterComponentCommand;

public interface RecoveredComponentCommandService {

    Result<RecoveredComponent, ApplicationError> handle(RegisterComponentCommand command);
}