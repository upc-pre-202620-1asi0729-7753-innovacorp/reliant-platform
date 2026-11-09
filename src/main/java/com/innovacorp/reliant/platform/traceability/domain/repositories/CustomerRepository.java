package com.innovacorp.reliant.platform.traceability.domain.repositories;

import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Customer;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.Ruc;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Customer save(Customer customer);

    Optional<Customer> findById(Long id);

    List<Customer> findAllBySupplierOrganizationId(OrganizationId supplierOrganizationId);

    List<Customer> findAllByLinkedAssetOwnerOrganizationId(Long assetOwnerOrganizationId);

    boolean existsBySupplierOrganizationIdAndRuc(OrganizationId supplierOrganizationId, Ruc ruc);
}