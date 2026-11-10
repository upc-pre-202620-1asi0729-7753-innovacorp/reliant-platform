package com.innovacorp.reliant.platform.traceability.application.commandservices;

import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.CompleteRecuperationCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.LinkSessionToRecuperationCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.OpenRecuperationCommand;

public interface RecuperationCommandService {

    Result<Recuperation, ApplicationError> handle(OpenRecuperationCommand command);

    Result<Recuperation, ApplicationError> handle(CompleteRecuperationCommand command);

    Result<Recuperation, ApplicationError> handle(LinkSessionToRecuperationCommand command);
}