package com.innovacorp.reliant.platform.reporting.application.commandservices;

import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.GeneratedReport;
import com.innovacorp.reliant.platform.reporting.domain.model.commands.RegisterGeneratedReportCommand;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;

public interface GeneratedReportCommandService {
    Result<GeneratedReport, ApplicationError> handle(RegisterGeneratedReportCommand command);
}
