package com.innovacorp.reliant.platform.traceability.interfaces.rest.transform;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Customer;
import com.innovacorp.reliant.platform.traceability.interfaces.rest.resources.CustomerResource;

public class CustomerResourceFromEntityAssembler {

    public static CustomerResource toResourceFromEntity(Customer entity) {
        return new CustomerResource(
                entity.getId(), entity.getSupplierOrganizationIdValue(),
                entity.getLinkedAssetOwnerOrganizationId(), entity.getLegalName(),
                entity.getRucValue(), entity.getMineSite());
    }
}