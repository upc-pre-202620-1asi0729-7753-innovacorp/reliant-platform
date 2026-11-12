package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.adapters;

import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.events.DomainEventPublisher;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Customer;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.Ruc;
import com.innovacorp.reliant.platform.traceability.domain.repositories.CustomerRepository;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.assemblers.CustomerPersistenceAssembler;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.repositories.CustomerPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerPersistenceRepository persistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public CustomerRepositoryImpl(CustomerPersistenceRepository persistenceRepository, DomainEventPublisher domainEventPublisher) {
        this.persistenceRepository = persistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Customer save(Customer customer) {
        boolean isNew = customer.getId() == null;
        var savedEntity = persistenceRepository.save(CustomerPersistenceAssembler.toPersistenceFromDomain(customer));
        var saved = CustomerPersistenceAssembler.toDomainFromPersistence(savedEntity);
        if (isNew) {
            saved.onCreated();
            domainEventPublisher.publishAndClearEvents(saved);
        }
        return saved;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return persistenceRepository.findById(id).map(CustomerPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Customer> findAllBySupplierOrganizationId(OrganizationId supplierOrganizationId) {
        return persistenceRepository.findAllBySupplierOrganizationId(supplierOrganizationId).stream()
                .map(CustomerPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public List<Customer> findAllByLinkedAssetOwnerOrganizationId(Long assetOwnerOrganizationId) {
        return persistenceRepository.findAllByLinkedAssetOwnerOrganizationIdOrderByLegalName(assetOwnerOrganizationId).stream()
                .map(CustomerPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public boolean existsBySupplierOrganizationIdAndRuc(OrganizationId supplierOrganizationId, Ruc ruc) {
        return persistenceRepository.countBySupplierOrganizationIdAndRuc(supplierOrganizationId, ruc) > 0;
    }
}
