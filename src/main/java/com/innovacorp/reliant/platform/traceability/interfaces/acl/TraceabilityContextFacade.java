package com.innovacorp.reliant.platform.traceability.interfaces.acl;

/**
 * Exposes Traceability capabilities to other bounded contexts.
 * Uses identifiers and boolean results at the context boundary.
 */
public interface TraceabilityContextFacade {

    /**
     * Returns the supplier organization ID, or zero when the recuperation does not exist.
     */
    Long fetchRecuperationSupplierOrganizationId(Long recuperationId);

    /**
     * Links a spray session to a recuperation.
     * The first link moves the recuperation from RECEIVED to IN_PROGRESS.
     */
    boolean linkSessionToRecuperation(Long recuperationId, Long sessionId);
}