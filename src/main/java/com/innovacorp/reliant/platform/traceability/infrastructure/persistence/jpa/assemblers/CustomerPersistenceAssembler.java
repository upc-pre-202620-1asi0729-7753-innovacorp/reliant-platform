package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Customer;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.entities.CustomerPersistenceEntity;

public final class CustomerPersistenceAssembler {

    private CustomerPersistenceAssembler() {
    }

    public static Customer toDomainFromPersistence(CustomerPersistenceEntity entity) {
        if (entity == null) return null;
        return Customer.reconstitute(entity.getId(), entity.getSupplierOrganizationId(), entity.getLinkedAssetOwnerOrganizationId(),
                entity.getLegalName(), entity.getRuc(), entity.getMineSite());
    }

    public static CustomerPersistenceEntity toPersistenceFromDomain(Customer customer) {
        if (customer == null) return null;
        var entity = new CustomerPersistenceEntity();
        if (customer.getId() != null) {
            entity.setId(customer.getId());
        }
        entity.setSupplierOrganizationId(customer.getSupplierOrganizationId());
        entity.setLinkedAssetOwnerOrganizationId(customer.getLinkedAssetOwnerOrganizationId());
        entity.setLegalName(customer.getLegalName());
        entity.setRuc(customer.getRuc());
        entity.setMineSite(customer.getMineSite());
        return entity;
    }
}
