package com.innovacorp.reliant.platform.reporting.application.queryservices;

import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.ReportTemplate;
import com.innovacorp.reliant.platform.reporting.domain.model.queries.GetAllReportTemplatesByOrganizationIdQuery;
import com.innovacorp.reliant.platform.reporting.domain.model.queries.GetReportTemplateByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ReportTemplateQueryService {
    List<ReportTemplate> handle(GetAllReportTemplatesByOrganizationIdQuery query);

    Optional<ReportTemplate> handle(GetReportTemplateByIdQuery query);
}
