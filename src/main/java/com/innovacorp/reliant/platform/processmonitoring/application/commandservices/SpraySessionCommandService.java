package com.innovacorp.reliant.platform.processmonitoring.application.commandservices;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates.SpraySession;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.commands.*;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.ProcessReading;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.SprayPass;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;

import java.util.List;

public interface SpraySessionCommandService {

    Result<SpraySession, ApplicationError> handle(StartSpraySessionCommand command);

    Result<SpraySession, ApplicationError> handle(CompleteSpraySessionCommand command);

    Result<SpraySession, ApplicationError> handle(AbortSpraySessionCommand command);

    Result<List<ProcessReading>, ApplicationError> handle(IngestProcessReadingsCommand command);

    Result<SprayPass, ApplicationError> handle(RegisterSprayPassCommand command);
}