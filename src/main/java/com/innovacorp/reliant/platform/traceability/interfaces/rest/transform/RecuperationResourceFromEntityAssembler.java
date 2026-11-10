package com.innovacorp.reliant.platform.traceability.interfaces.rest.transform;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.LinkedSessionResource;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.RecuperationResource;

public class RecuperationResourceFromEntityAssembler {

    public static RecuperationResource toResourceFromEntity(Recuperation recuperation) {
        return new RecuperationResource(
                recuperation.getId(), recuperation.getWorkOrderNumberValue(),
                recuperation.getManufacturingOrderNumber(), recuperation.getComponentIdValue(),
                recuperation.getCustomerIdValue(), recuperation.getSupplierOrganizationIdValue(),
                recuperation.getSegment(), recuperation.getOperation(), recuperation.getWeightValue(),
                recuperation.getWeightUnitSymbol(), recuperation.getHourmeterAtEntry(),
                recuperation.getPowderSupplier(), recuperation.getPowderLotNumber(),
                recuperation.getPowderChemicalComposition(), recuperation.getStatus().name(),
                recuperation.getReceivedAt(), recuperation.getCompletedAt(),
                recuperation.getLinkedSessionIds().stream()
                        .map(LinkedSessionResource::new)
                        .toList());
    }
}