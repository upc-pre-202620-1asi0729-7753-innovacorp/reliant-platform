package com.innovacorp.reliant.platform.traceability.interfaces.rest.transform;

import com.innovacorp.reliant.platform.traceability.domain.model.commands.RegisterCustomerCommand;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.RegisterCustomerResource;

public class RegisterCustomerCommandFromResourceAssembler {

    public static RegisterCustomerCommand toCommandFromResource(RegisterCustomerResource resource) {
        // The command service gets the supplier organization ID from the authenticated user.
        return new RegisterCustomerCommand(
                null, resource.linkedAssetOwnerOrganizationId(),
                resource.legalName(), resource.ruc(), resource.mineSite());
    }
}