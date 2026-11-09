package com.innovacorp.reliant.platform.traceability.domain.model.events;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Customer;

public record CustomerRegisteredEvent(Long customerId, Long supplierOrganizationId, String legalName) {
    public static CustomerRegisteredEvent from(Customer customer) {
        return new CustomerRegisteredEvent(
                customer.getId(), customer.getSupplierOrganizationIdValue(), customer.getLegalName());
    }
}