
package com.innovacorp.reliant.platform.iam.interfaces.events;

/**
 * Integration event published by the {@code iam} bounded context when a new organization has been
 * registered and persisted (sign-up of its first administrator).
 *
 * <p>This is the published language of {@code iam}. Other bounded contexts (Fault Diagnosis seeds the
 * FESA default rules of a new supplier, Traceability links the customers of a new asset owner) listen to
 * this event rather than to internal domain events, which are an internal concern of the iam domain.
 * Every field is a plain type: no {@code iam.domain} type crosses the boundary.</p>
 *
 * @param organizationId   the persisted organization identifier
 * @param legalName        the organization legal name
 * @param ruc              the Peruvian tax id (11 digits)
 * @param organizationType {@code RECUPERATION_SUPPLIER} or {@code ASSET_OWNER}
 */
public record OrganizationRegisteredIntegrationEvent(Long organizationId, String legalName, String ruc, String organizationType) {

    public boolean isRecuperationSupplier() {
        return "RECUPERATION_SUPPLIER".equals(organizationType);
    }

    public boolean isAssetOwner() {
        return "ASSET_OWNER".equals(organizationType);
    }
}
