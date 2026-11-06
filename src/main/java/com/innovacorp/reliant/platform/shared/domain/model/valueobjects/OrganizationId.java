package com.innovacorp.reliant.platform.shared.domain.model.valueobjects;

/**
 * OrganizationId Value Object. Identifies the tenant (supplier or asset owner) that owns an aggregate.
 */
public record OrganizationId(Long organizationId) {
    public OrganizationId {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("Organization id must be a positive number");
        }
    }
}