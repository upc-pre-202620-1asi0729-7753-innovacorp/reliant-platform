package com.innovacorp.reliant.platform.iam.domain.model.queries;

import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

public record GetAllUsersByOrganizationIdQuery(OrganizationId organizationId) {
}
