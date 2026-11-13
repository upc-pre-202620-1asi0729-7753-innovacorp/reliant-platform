package com.innovacorp.reliant.platform.reporting.interfaces.rest.transform;

import com.innovacorp.reliant.platform.reporting.domain.model.commands.CreateReportTemplateCommand;
import com.innovacorp.reliant.platform.reporting.domain.model.commands.ReportSectionItem;
import com.innovacorp.reliant.platform.reporting.domain.model.commands.ReportWidgetItem;
import com.innovacorp.reliant.platform.reporting.domain.model.commands.UpdateReportTemplateCommand;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.resources.CreateReportTemplateResource;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.resources.ReportSectionResource;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.resources.UpdateReportTemplateResource;

import java.util.List;

public class ReportTemplateCommandsFromResourceAssembler {
    /** The organization and owner are filled by the command service from the authenticated user. */
    public static CreateReportTemplateCommand toCommandFromResource(CreateReportTemplateResource r) {
        return new CreateReportTemplateCommand(null, null, r.name(), r.description(), r.reportKind(), r.primaryColor(), r.secondaryColor(), r.headerText(),
                r.footerText(), r.pageSize(), r.orientation(), r.showSignatureBlock(), r.defaultForKind(), toItems(r.sections()));
    }

    public static UpdateReportTemplateCommand toCommandFromResource(Long reportTemplateId, UpdateReportTemplateResource r) {
        return new UpdateReportTemplateCommand(reportTemplateId, r.name(), r.description(), r.primaryColor(), r.secondaryColor(), r.headerText(), r.footerText(),
                r.pageSize(), r.orientation(), r.showSignatureBlock(), r.defaultForKind(), toItems(r.sections()));
    }

    private static List<ReportSectionItem> toItems(List<ReportSectionResource> sections) {
        return sections.stream().map(s -> new ReportSectionItem(s.title(), s.sectionOrder(), s.widgets().stream()
                .map(w -> new ReportWidgetItem(w.position(), w.variableSource(), w.variableCode(), w.variableLabel(), w.viewType(), w.aggregation(), w.width()))
                .toList())).toList();
    }
}
