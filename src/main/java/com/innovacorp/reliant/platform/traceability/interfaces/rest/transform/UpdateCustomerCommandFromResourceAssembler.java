package com.innovacorp.reliant.platform.traceability.interfaces.rest.transform;

import com.innovacorp.reliant.platform.traceability.domain.model.commands.UpdateCustomerCommand;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.UpdateCustomerResource;

public class UpdateCustomerCommandFromResourceAssembler {
    public static UpdateCustomerCommand toCommandFromResource(Long customerId, UpdateCustomerResource resource) {
        return new UpdateCustomerCommand(customerId, resource.linkedAssetOwnerOrganizationId(), resource.legalName(), resource.mineSite());
    }
}
