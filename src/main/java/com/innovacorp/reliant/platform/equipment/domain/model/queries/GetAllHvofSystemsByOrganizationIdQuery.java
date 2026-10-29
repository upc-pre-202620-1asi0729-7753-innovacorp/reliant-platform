package com.innovacorp.reliant.platform.equipment.domain.model.queries;

import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

public record GetAllHvofSystemsByOrganizationIdQuery(OrganizationId organizationId) {
    public GetAllHvofSystemsByOrganizationIdQuery {
        if (organizationId == null) throw new IllegalArgumentException("shared.error.organizationId.invalid");
    }
}