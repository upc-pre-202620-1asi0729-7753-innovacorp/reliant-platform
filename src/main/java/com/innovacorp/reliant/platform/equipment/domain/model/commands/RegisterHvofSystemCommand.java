package com.innovacorp.reliant.platform.equipment.domain.model.commands;

/**
 * Register HVOF System Command. Raw values: every one is validated by a value object inside the aggregate constructor.
 */
public record RegisterHvofSystemCommand(Long organizationId,
                                        String code,
                                        String serialNumber,
                                        String systemManufacturer,
                                        String systemModel,
                                        String fuelType) {
}