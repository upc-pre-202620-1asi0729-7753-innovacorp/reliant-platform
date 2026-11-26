package com.innovacorp.reliant.platform.iam.interfaces.acl;

/**
 * ACL facade that exposes IAM bounded context capabilities to other contexts. Primitives only: no
 * {@code iam.domain} type leaks out, and {@code 0L} / an empty string are the "not found" sentinels, never an
 * exception, because the caller treats a miss as a routine outcome to check.
 */
public interface IamContextFacade {
    /** @return the id of the ASSET_OWNER organization registered with that RUC, or 0L when there is none. */
    Long fetchAssetOwnerOrganizationIdByRuc(String ruc);

    /** @return the legal name of the organization, or an empty string when it does not exist. */
    String fetchOrganizationLegalNameById(Long organizationId);
}
