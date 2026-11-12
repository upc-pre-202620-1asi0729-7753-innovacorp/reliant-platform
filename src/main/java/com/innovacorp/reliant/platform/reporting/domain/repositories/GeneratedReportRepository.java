package com.innovacorp.reliant.platform.reporting.domain.repositories;

import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.GeneratedReport;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportSubjectType;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

import java.util.List;
import java.util.Optional;

public interface GeneratedReportRepository {
    GeneratedReport save(GeneratedReport report);

    Optional<GeneratedReport> findById(Long id);

    List<GeneratedReport> findAllByOrganizationId(OrganizationId organizationId, ReportSubjectType subjectType, Long subjectId);
}
