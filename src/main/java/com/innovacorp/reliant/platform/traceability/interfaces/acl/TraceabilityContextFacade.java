package com.innovacorp.reliant.platform.traceability.interfaces.acl;

/**
 * ACL facade that exposes Traceability capabilities to other bounded contexts. Primitives only, queries only:
 * facts that change Traceability (a session started for a recuperation) arrive as integration events.
 */
public interface TraceabilityContextFacade {
    /** @return the supplier organization id of the recuperation, or 0L when it does not exist. */
    Long fetchRecuperationSupplierOrganizationId(Long recuperationId);
}