package com.innovacorp.reliant.platform.traceability.interfaces.rest.transform;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.LinkedSessionResource;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.RecuperationResource;

public class RecuperationResourceFromEntityAssembler {
    public static RecuperationResource toResourceFromEntity(Recuperation r) {
        return new RecuperationResource(r.getId(), r.getWorkOrderNumberValue(), r.getManufacturingOrderNumber(), r.getComponentIdValue(), r.getCustomerIdValue(),
                r.getSupplierOrganizationIdValue(), r.getSegment(), r.getOperation(), r.getWeightValue(), r.getWeightUnitSymbol(), r.getHourmeterAtEntry(),
                r.getPowderSupplier(), r.getPowderLotNumber(), r.getPowderChemicalComposition(), r.getStatus().name(), r.getReceivedAt(), r.getCompletedAt(),
                r.getLinkedSessionIds().stream().map(LinkedSessionResource::new).toList());
    }
}
