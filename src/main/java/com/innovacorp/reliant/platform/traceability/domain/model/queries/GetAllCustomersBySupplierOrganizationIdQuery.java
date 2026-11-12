package com.innovacorp.reliant.platform.traceability.domain.model.queries;

import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

public record GetAllCustomersBySupplierOrganizationIdQuery(OrganizationId supplierOrganizationId) {
}
