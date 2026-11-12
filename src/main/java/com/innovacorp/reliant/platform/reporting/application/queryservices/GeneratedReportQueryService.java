package com.innovacorp.reliant.platform.reporting.application.queryservices;

import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.GeneratedReport;
import com.innovacorp.reliant.platform.reporting.domain.model.queries.GetAllGeneratedReportsByOrganizationIdQuery;
import com.innovacorp.reliant.platform.reporting.domain.model.queries.GetGeneratedReportByIdQuery;

import java.util.List;
import java.util.Optional;

public interface GeneratedReportQueryService {
    List<GeneratedReport> handle(GetAllGeneratedReportsByOrganizationIdQuery query);

    Optional<GeneratedReport> handle(GetGeneratedReportByIdQuery query);
}
