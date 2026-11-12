package com.innovacorp.reliant.platform.traceability.interfaces.rest.transform;

import com.innovacorp.reliant.platform.traceability.domain.model.commands.RegisterCustomerCommand;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.RegisterCustomerResource;

public class RegisterCustomerCommandFromResourceAssembler {
    /** supplierOrganizationId is null here on purpose: the command service fills it from the authenticated user. */
    public static RegisterCustomerCommand toCommandFromResource(RegisterCustomerResource resource) {
        return new RegisterCustomerCommand(null, resource.linkedAssetOwnerOrganizationId(), resource.legalName(), resource.ruc(), resource.mineSite());
    }
}
