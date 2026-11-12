package com.innovacorp.reliant.platform.reporting.interfaces.rest.transform;

import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.GeneratedReport;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.resources.GeneratedReportResource;

public class GeneratedReportResourceFromEntityAssembler {
    public static GeneratedReportResource toResourceFromEntity(GeneratedReport r) {
        return new GeneratedReportResource(r.getId(), r.getReportTemplateId(), r.getOrganizationIdValue(), r.getGeneratedByUserId(),
                r.getSubjectType().name().toLowerCase(), r.getSubjectId(), r.getFormat(), r.getFileName(), r.getPeriodFrom(), r.getPeriodTo(), r.getGeneratedAt());
    }
}
