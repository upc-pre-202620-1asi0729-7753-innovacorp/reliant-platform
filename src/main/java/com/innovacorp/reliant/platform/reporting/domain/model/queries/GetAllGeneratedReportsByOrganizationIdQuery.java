package com.innovacorp.reliant.platform.reporting.domain.model.queries;

import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportSubjectType;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

/** subjectType and subjectId are optional filters. */
public record GetAllGeneratedReportsByOrganizationIdQuery(OrganizationId organizationId, ReportSubjectType subjectType, Long subjectId) {
}
