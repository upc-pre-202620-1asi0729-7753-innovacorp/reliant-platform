package com.innovacorp.reliant.platform.traceability.application.queryservices;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Customer;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllCustomersByLinkedAssetOwnerOrganizationIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetAllCustomersBySupplierOrganizationIdQuery;
import com.innovacorp.reliant.platform.traceability.domain.model.queries.GetCustomerByIdQuery;

import java.util.List;
import java.util.Optional;

public interface CustomerQueryService {
    Optional<Customer> handle(GetCustomerByIdQuery query);

    List<Customer> handle(GetAllCustomersBySupplierOrganizationIdQuery query);

    List<Customer> handle(GetAllCustomersByLinkedAssetOwnerOrganizationIdQuery query);
}
