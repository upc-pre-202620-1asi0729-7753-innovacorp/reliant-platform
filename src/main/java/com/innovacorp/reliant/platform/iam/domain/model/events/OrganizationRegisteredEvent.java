package com.innovacorp.reliant.platform.iam.domain.model.events;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.Organization;

/**
 * Domain event published when a new organization is persisted.
 */
public record OrganizationRegisteredEvent(Long organizationId, String legalName, String organizationType) {

    public static OrganizationRegisteredEvent from(Organization organization) {
        return new OrganizationRegisteredEvent(
                organization.getId(),
                organization.getLegalName(),
                organization.getOrganizationType().name()
        );
    }
}