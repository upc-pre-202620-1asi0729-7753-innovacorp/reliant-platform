package com.innovacorp.reliant.platform.reporting.application.internal.commandservices;

import com.innovacorp.reliant.platform.reporting.application.commandservices.ReportTemplateCommandService;
import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.ReportTemplate;
import com.innovacorp.reliant.platform.reporting.domain.model.commands.*;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportKind;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.TemplateStatus;
import com.innovacorp.reliant.platform.reporting.domain.repositories.ReportTemplateRepository;
import com.innovacorp.reliant.platform.shared.application.context.CurrentUser;
import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Templates belong to the organization of the authenticated user; administrators and quality engineers manage them.
 */
@Service
public class ReportTemplateCommandServiceImpl implements ReportTemplateCommandService {
    private static final String PRIMARY = "#0E1A2B";
    private static final String SECONDARY = "#D4781C";

    private final ReportTemplateRepository templateRepository;
    private final CurrentUserProvider currentUserProvider;

    public ReportTemplateCommandServiceImpl(ReportTemplateRepository templateRepository, CurrentUserProvider currentUserProvider) {
        this.templateRepository = templateRepository;
        this.currentUserProvider = currentUserProvider;
    }

    private static boolean canManage(CurrentUser user) {
        return user.hasRole("ROLE_ORG_ADMIN") || user.hasRole("ROLE_QUALITY_ENGINEER");
    }

    private static ApplicationError forbidden() {
        return new ApplicationError("ACCESS_FORBIDDEN", "Access forbidden", "Only organization administrators and quality engineers manage report templates");
    }

    @Override
    @Transactional
    public Result<ReportTemplate, ApplicationError> handle(CreateReportTemplateCommand command) {
        var currentUser = currentUserProvider.current();
        if (!canManage(currentUser)) return Result.failure(forbidden());
        var organizationId = new OrganizationId(currentUser.organizationId());
        if (templateRepository.existsByOrganizationIdAndName(organizationId, command.name() == null ? "" : command.name().trim(), null)) {
            return Result.failure(ApplicationError.conflict("report-template", command.name()));
        }
        try {
            var template = new ReportTemplate(new CreateReportTemplateCommand(currentUser.organizationId(), currentUser.userId(), command.name(),
                    command.description(), command.reportKind(), command.primaryColor(), command.secondaryColor(), command.headerText(), command.footerText(),
                    command.pageSize(), command.orientation(), command.showSignatureBlock(), command.defaultForKind(), command.sections()));
            if (template.isDefaultForKind()) clearDefaults(organizationId, template.getReportKind(), null);
            return Result.success(templateRepository.save(template));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("ReportTemplate", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Report template creation", e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<ReportTemplate, ApplicationError> handle(UpdateReportTemplateCommand command) {
        var currentUser = currentUserProvider.current();
        var template = templateRepository.findById(command.reportTemplateId()).filter(t -> t.isOwnedBy(currentUser.organizationId()));
        if (template.isEmpty()) {
            return Result.failure(ApplicationError.notFound("report-template", String.valueOf(command.reportTemplateId())));
        }
        if (!canManage(currentUser)) return Result.failure(forbidden());
        var organizationId = new OrganizationId(currentUser.organizationId());
        if (templateRepository.existsByOrganizationIdAndName(organizationId, command.name() == null ? "" : command.name().trim(), template.get().getId())) {
            return Result.failure(ApplicationError.conflict("report-template", command.name()));
        }
        try {
            template.get().update(command);
            if (template.get().isDefaultForKind()) clearDefaults(organizationId, template.get().getReportKind(), template.get().getId());
            return Result.success(templateRepository.save(template.get()));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("ReportTemplate", e.getMessage()));
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("report-template-status", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Report template update", e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<ReportTemplate, ApplicationError> handle(ChangeReportTemplateStatusCommand command) {
        var currentUser = currentUserProvider.current();
        var template = templateRepository.findById(command.reportTemplateId()).filter(t -> t.isOwnedBy(currentUser.organizationId()));
        if (template.isEmpty()) {
            return Result.failure(ApplicationError.notFound("report-template", String.valueOf(command.reportTemplateId())));
        }
        if (!canManage(currentUser)) return Result.failure(forbidden());
        try {
            template.get().changeStatus(TemplateStatus.fromString(command.status()));
            return Result.success(templateRepository.save(template.get()));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("status", e.getMessage()));
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("report-template-status", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Report template status change", e.getMessage()));
        }
    }

    /** Idempotent: seeds the standard templates only when the organization has none. */
    @Override
    @Transactional
    public void handle(SeedDefaultReportTemplatesCommand command) {
        var organizationId = new OrganizationId(command.organizationId());
        if (templateRepository.existsByOrganizationId(organizationId)) return;
        defaultTemplates(command.organizationId(), command.ownerUserId()).forEach(templateRepository::save);
    }

    private void clearDefaults(OrganizationId organizationId, ReportKind kind, Long exceptId) {
        templateRepository.findDefaultsByOrganizationIdAndKind(organizationId, kind).stream()
                .filter(other -> exceptId == null || !other.getId().equals(exceptId))
                .forEach(other -> {
                    other.clearDefault();
                    templateRepository.save(other);
                });
    }

    private static ReportWidgetItem process(int position, String code, String label, String view, String aggregation, String width) {
        return new ReportWidgetItem(position, "process", code, label, view, aggregation, width);
    }

    private static List<ReportTemplate> defaultTemplates(Long organizationId, Long ownerUserId) {
        var certificate = new ReportTemplate(new CreateReportTemplateCommand(organizationId, ownerUserId, "Certificado de calidad",
                "Certificado de recubrimiento HVOF por orden de recuperación", "quality_certificate", PRIMARY, SECONDARY,
                "Certificado de calidad de recubrimiento HVOF", "Documento generado con Reliant", "a4", "portrait", true, true, List.of(
                        new ReportSectionItem("Orden y componente", 1, List.of(
                                new ReportWidgetItem(1, "recuperation", "work_order_number", "Orden de trabajo", "value", "last", "half"),
                                new ReportWidgetItem(2, "recuperation", "component_serial", "Serie del componente", "value", "last", "half"),
                                new ReportWidgetItem(3, "recuperation", "customer_name", "Cliente", "value", "last", "half"),
                                new ReportWidgetItem(4, "session", "recipe", "Receta aplicada", "value", "last", "half"))),
                        new ReportSectionItem("Parámetros de proceso", 2, List.of(
                                process(1, "fuel_gas_flow", "Flujo de combustible", "min_max", "range", "half"),
                                process(2, "oxygen_flow", "Flujo de oxígeno", "min_max", "range", "half"),
                                process(3, "powder_feed_rate", "Alimentación de polvo", "min_max", "range", "half"),
                                process(4, "coolant_outlet_temp", "Temperatura de refrigerante", "min_max", "range", "half"))),
                        new ReportSectionItem("Cumplimiento", 3, List.of(
                                new ReportWidgetItem(1, "session", "compliance", "Cumplimiento de banda nominal", "value", "avg", "half"),
                                new ReportWidgetItem(2, "fault_cases", "cases", "Casos de falla de la sesión", "table", "count", "full"))))));

        var sessionReport = new ReportTemplate(new CreateReportTemplateCommand(organizationId, ownerUserId, "Reporte de sesión",
                "Resumen operativo de una sesión de rociado con tendencias y desviaciones", "session_report", PRIMARY, SECONDARY,
                "Reporte de sesión de rociado HVOF", "Documento generado con Reliant", "a4", "portrait", false, true, List.of(
                        new ReportSectionItem("Resumen", 1, List.of(
                                new ReportWidgetItem(1, "session", "system", "Sistema HVOF", "value", "last", "half"),
                                new ReportWidgetItem(2, "session", "recipe", "Receta", "value", "last", "half"),
                                new ReportWidgetItem(3, "session", "duration", "Duración", "value", "last", "half"),
                                new ReportWidgetItem(4, "session", "compliance", "Cumplimiento", "value", "avg", "half"))),
                        new ReportSectionItem("Tendencias", 2, List.of(
                                process(1, "fuel_gas_flow", "Flujo de combustible", "trend", "avg", "full"),
                                process(2, "oxygen_flow", "Flujo de oxígeno", "trend", "avg", "full"),
                                process(3, "powder_feed_rate", "Alimentación de polvo", "trend", "avg", "full"))),
                        new ReportSectionItem("Desviaciones", 3, List.of(
                                new ReportWidgetItem(1, "session", "deviations", "Lecturas fuera de banda", "table", "count", "full"),
                                new ReportWidgetItem(2, "fault_cases", "cases", "Casos de falla", "table", "count", "full"))))));
        return List.of(certificate, sessionReport);
    }
}
