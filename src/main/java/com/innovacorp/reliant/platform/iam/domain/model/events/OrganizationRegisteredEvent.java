package com.innovacorp.reliant.platform.iam.domain.model.events;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.Organization;

/**
 * Domain event published when a new organization is persisted. Internal to the iam bounded context:
 * other contexts subscribe to {@code OrganizationRegisteredIntegrationEvent}, never to this one.
 */
public record OrganizationRegisteredEvent(Long organizationId, String legalName, String ruc, String organizationType) {

    public static OrganizationRegisteredEvent from(Organization organization) {
        return new OrganizationRegisteredEvent(organization.getId(), organization.getLegalName(), organization.getRucValue(),
                organization.getOrganizationType().name());
    }
}