package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.queries;

import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

public record GetAllFaultRulesByOrganizationIdQuery(OrganizationId organizationId) {
}
