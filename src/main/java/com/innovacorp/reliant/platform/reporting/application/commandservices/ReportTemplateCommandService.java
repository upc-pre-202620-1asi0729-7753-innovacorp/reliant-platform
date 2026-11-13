package com.innovacorp.reliant.platform.reporting.application.commandservices;

import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.ReportTemplate;
import com.innovacorp.reliant.platform.reporting.domain.model.commands.ChangeReportTemplateStatusCommand;
import com.innovacorp.reliant.platform.reporting.domain.model.commands.CreateReportTemplateCommand;
import com.innovacorp.reliant.platform.reporting.domain.model.commands.SeedDefaultReportTemplatesCommand;
import com.innovacorp.reliant.platform.reporting.domain.model.commands.UpdateReportTemplateCommand;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;

public interface ReportTemplateCommandService {
    Result<ReportTemplate, ApplicationError> handle(CreateReportTemplateCommand command);

    Result<ReportTemplate, ApplicationError> handle(UpdateReportTemplateCommand command);

    Result<ReportTemplate, ApplicationError> handle(ChangeReportTemplateStatusCommand command);

    void handle(SeedDefaultReportTemplatesCommand command);
}
