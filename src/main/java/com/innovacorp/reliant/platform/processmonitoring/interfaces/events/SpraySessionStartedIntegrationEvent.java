package com.innovacorp.reliant.platform.processmonitoring.interfaces.events;

/**
 * Integration event published by the {@code processmonitoring} bounded context when a spray session has been
 * started and persisted for a recuperation order.
 *
 * <p>Published language of Process Monitoring: Traceability listens to it to link the session to its recuperation
 * (RECEIVED → IN_PROGRESS on the first session), instead of Process Monitoring writing into Traceability through the
 * ACL facade. The facade stays a read-only query surface; facts travel as events.</p>
 *
 * @param spraySessionId the persisted session identifier
 * @param organizationId the supplier organization that operates the session
 * @param hvofSystemId   the HVOF system the session runs on
 * @param recuperationId the recuperation order the session works on
 * @param recipeNumber   the active recipe applied during the session
 */
public record SpraySessionStartedIntegrationEvent(Long spraySessionId, Long organizationId, Long hvofSystemId, Long recuperationId, Integer recipeNumber) {
}