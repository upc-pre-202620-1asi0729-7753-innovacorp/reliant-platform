package com.innovacorp.reliant.platform.traceability.interfaces.acl;

/**
 * ACL facade that exposes Traceability capabilities to other bounded contexts. Primitives only.
 */
public interface TraceabilityContextFacade {
    /** @return the supplier organization id of the recuperation, or 0L when it does not exist. */
    Long fetchRecuperationSupplierOrganizationId(Long recuperationId);

    /** Links a spray session to a recuperation (RECEIVED → IN_PROGRESS on first link). @return true when linked. */
    boolean linkSessionToRecuperation(Long recuperationId, Long sessionId);
}
