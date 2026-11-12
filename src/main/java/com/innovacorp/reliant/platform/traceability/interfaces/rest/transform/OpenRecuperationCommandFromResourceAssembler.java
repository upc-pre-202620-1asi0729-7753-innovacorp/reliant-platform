package com.innovacorp.reliant.platform.traceability.interfaces.rest.transform;

import com.innovacorp.reliant.platform.traceability.domain.model.commands.OpenRecuperationCommand;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.OpenRecuperationResource;

public class OpenRecuperationCommandFromResourceAssembler {
    public static OpenRecuperationCommand toCommandFromResource(OpenRecuperationResource r) {
        return new OpenRecuperationCommand(r.workOrderNumber(), r.manufacturingOrderNumber(), r.componentId(), r.customerId(), r.segment(), r.operation(),
                r.weightValue(), r.weightUnitSymbol(), r.hourmeterAtEntry(), r.powderSupplier(), r.powderLotNumber(), r.powderChemicalComposition(), r.receivedAt());
    }
}
