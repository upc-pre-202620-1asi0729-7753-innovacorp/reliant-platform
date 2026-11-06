package com.innovacorp.reliant.platform.equipment.domain.model.events;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;

/**
 * Domain event published when a new HVOF system is persisted.
 */
public record HvofSystemRegisteredEvent(Long hvofSystemId, Long organizationId, String code, String fuelType) {

    public static HvofSystemRegisteredEvent from(HvofSystem hvofSystem) {
        return new HvofSystemRegisteredEvent(
                hvofSystem.getId(),
                hvofSystem.getOrganizationIdValue(),
                hvofSystem.getCodeValue(),
                hvofSystem.getFuelType().name());
    }
}