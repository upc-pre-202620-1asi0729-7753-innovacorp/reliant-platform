package com.innovacorp.reliant.platform.reporting.interfaces.rest.transform;

import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.ReportTemplate;
import com.innovacorp.reliant.platform.reporting.domain.model.entities.ReportSection;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportWidget;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.resources.ReportSectionResource;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.resources.ReportTemplateResource;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.resources.ReportWidgetResource;

public class ReportTemplateResourceFromEntityAssembler {
    public static ReportTemplateResource toResourceFromEntity(ReportTemplate t) {
        return new ReportTemplateResource(t.getId(), t.getOrganizationIdValue(), t.getOwnerUserId(), t.getName(), t.getDescription(),
                t.getReportKind().name().toLowerCase(), t.getPrimaryColor().value(), t.getSecondaryColor().value(), t.getHeaderText(), t.getFooterText(),
                t.getPageSize().name().toLowerCase(), t.getOrientation().name().toLowerCase(), t.isShowSignatureBlock(), t.isDefaultForKind(),
                t.getStatus().name().toLowerCase(), t.getSections().stream().map(ReportTemplateResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    public static ReportSectionResource toResourceFromEntity(ReportSection s) {
        return new ReportSectionResource(s.getTitle(), s.getSectionOrder(), s.getWidgets().stream().map(ReportTemplateResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    public static ReportWidgetResource toResourceFromEntity(ReportWidget w) {
        return new ReportWidgetResource(w.position(), w.variableSource().name().toLowerCase(), w.variableCode(), w.variableLabel(),
                w.viewType().name().toLowerCase(), w.aggregation().name().toLowerCase(), w.width().name().toLowerCase());
    }
}
