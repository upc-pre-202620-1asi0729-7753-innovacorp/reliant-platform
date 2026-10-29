package com.innovacorp.reliant.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record OrganizationId(Long organizationId) {
    public OrganizationId {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("shared.error.organizationId.invalid");
        }
    }
}