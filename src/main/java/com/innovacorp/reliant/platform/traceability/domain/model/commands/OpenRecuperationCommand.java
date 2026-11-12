package com.innovacorp.reliant.platform.traceability.domain.model.commands;

import java.time.LocalDate;

public record OpenRecuperationCommand(String workOrderNumber, String manufacturingOrderNumber, Long componentId, Long customerId,
                                      String segment, String operation, Double weightValue, String weightUnitSymbol, Integer hourmeterAtEntry,
                                      String powderSupplier, String powderLotNumber, String powderChemicalComposition, LocalDate receivedAt) {
}
