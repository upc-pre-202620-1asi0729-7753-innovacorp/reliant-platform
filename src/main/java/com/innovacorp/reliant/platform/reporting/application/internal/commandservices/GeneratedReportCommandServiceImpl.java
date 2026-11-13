package com.innovacorp.reliant.platform.reporting.application.internal.commandservices;

import com.innovacorp.reliant.platform.reporting.application.commandservices.GeneratedReportCommandService;
import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.GeneratedReport;
import com.innovacorp.reliant.platform.reporting.domain.model.commands.RegisterGeneratedReportCommand;
import com.innovacorp.reliant.platform.reporting.domain.repositories.GeneratedReportRepository;
import com.innovacorp.reliant.platform.reporting.domain.repositories.ReportTemplateRepository;
import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

@Service
public class GeneratedReportCommandServiceImpl implements GeneratedReportCommandService {
    private final GeneratedReportRepository reportRepository;
    private final ReportTemplateRepository templateRepository;
    private final CurrentUserProvider currentUserProvider;

    public GeneratedReportCommandServiceImpl(GeneratedReportRepository reportRepository, ReportTemplateRepository templateRepository,
                                             CurrentUserProvider currentUserProvider) {
        this.reportRepository = reportRepository;
        this.templateRepository = templateRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public Result<GeneratedReport, ApplicationError> handle(RegisterGeneratedReportCommand command) {
        var currentUser = currentUserProvider.current();
        var template = templateRepository.findById(command.reportTemplateId()).filter(t -> t.isOwnedBy(currentUser.organizationId()));
        if (template.isEmpty()) {
            return Result.failure(ApplicationError.notFound("report-template", String.valueOf(command.reportTemplateId())));
        }
        if (!template.get().isActive()) {
            return Result.failure(ApplicationError.businessRuleViolation("report-template-status", "Archived templates cannot be used to generate reports"));
        }
        try {
            var report = new GeneratedReport(new RegisterGeneratedReportCommand(command.reportTemplateId(), currentUser.organizationId(), currentUser.userId(),
                    command.subjectType(), command.subjectId(), command.fileName(), command.periodFrom(), command.periodTo()));
            return Result.success(reportRepository.save(report));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("GeneratedReport", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Generated report registration", e.getMessage()));
        }
    }
}
