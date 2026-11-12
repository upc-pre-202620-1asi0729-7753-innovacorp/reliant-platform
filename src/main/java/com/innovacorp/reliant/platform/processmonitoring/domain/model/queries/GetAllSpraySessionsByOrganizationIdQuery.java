package com.innovacorp.reliant.platform.processmonitoring.domain.model.queries;

import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

public record GetAllSpraySessionsByOrganizationIdQuery(OrganizationId organizationId) {
}