package com.innovacorp.reliant.platform.traceability.interfaces.rest.transform;

import com.innovacorp.reliant.platform.traceability.domain.model.commands.OpenRecuperationCommand;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.OpenRecuperationResource;

public class OpenRecuperationCommandFromResourceAssembler {

    public static OpenRecuperationCommand toCommandFromResource(OpenRecuperationResource resource) {
        return new OpenRecuperationCommand(
                resource.workOrderNumber(), resource.manufacturingOrderNumber(),
                resource.componentId(), resource.customerId(), resource.segment(), resource.operation(),
                resource.weightValue(), resource.weightUnitSymbol(), resource.hourmeterAtEntry(),
                resource.powderSupplier(), resource.powderLotNumber(),
                resource.powderChemicalComposition(), resource.receivedAt());
    }
}