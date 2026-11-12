package com.innovacorp.reliant.platform.traceability.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(name = "RecuperationResponse")
public record RecuperationResource(Long id, String workOrderNumber, String manufacturingOrderNumber, Long componentId, Long customerId, Long supplierOrganizationId,
                                   String segment, String operation, Double weightValue, String weightUnitSymbol, Integer hourmeterAtEntry, String powderSupplier,
                                   String powderLotNumber, String powderChemicalComposition, String status, LocalDate receivedAt, LocalDate completedAt,
                                   List<LinkedSessionResource> linkedSessions) {
}
