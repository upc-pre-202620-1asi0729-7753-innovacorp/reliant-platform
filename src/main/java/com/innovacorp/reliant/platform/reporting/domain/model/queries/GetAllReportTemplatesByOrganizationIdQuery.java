package com.innovacorp.reliant.platform.reporting.domain.model.queries;

import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportKind;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

/** reportKind is optional (null = every kind). */
public record GetAllReportTemplatesByOrganizationIdQuery(OrganizationId organizationId, ReportKind reportKind) {
}
