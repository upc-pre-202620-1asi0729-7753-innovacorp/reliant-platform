package com.innovacorp.reliant.platform.traceability.domain.model.commands;

public record RegisterComponentCommand(Long customerId, String serialNumber, String partNumber, String componentType,
                                       String machineManufacturer, String machineModel, Integer pcrTargetHours) {
}
