package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.adapters;

import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.events.DomainEventPublisher;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.WorkOrderNumber;
import com.innovacorp.reliant.platform.traceability.domain.repositories.RecuperationRepository;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.assemblers.RecuperationPersistenceAssembler;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.repositories.RecuperationPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RecuperationRepositoryImpl implements RecuperationRepository {

    private final RecuperationPersistenceRepository persistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public RecuperationRepositoryImpl(RecuperationPersistenceRepository persistenceRepository,
                                      DomainEventPublisher domainEventPublisher) {
        this.persistenceRepository = persistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Recuperation save(Recuperation recuperation) {
        boolean isNew = recuperation.getId() == null;
        var savedEntity = persistenceRepository.save(
                RecuperationPersistenceAssembler.toPersistenceFromDomain(recuperation));
        var saved = RecuperationPersistenceAssembler.toDomainFromPersistence(savedEntity);

        if (isNew) {
            saved.onCreated();
        }

        domainEventPublisher.publishAndClearEvents(recuperation);
        domainEventPublisher.publishAndClearEvents(saved);
        return saved;
    }

    @Override
    public Optional<Recuperation> findById(Long id) {
        return persistenceRepository.findById(id)
                .map(RecuperationPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Recuperation> findAllBySupplierOrganizationId(OrganizationId supplierOrganizationId) {
        return persistenceRepository.findAllBySupplierOrganizationId(supplierOrganizationId).stream()
                .map(RecuperationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Recuperation> findAllByCustomerId(CustomerId customerId) {
        return persistenceRepository.findAllByCustomerId(customerId).stream()
                .map(RecuperationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsBySupplierOrganizationIdAndWorkOrderNumber(
            OrganizationId supplierOrganizationId, WorkOrderNumber workOrderNumber) {
        return persistenceRepository.countBySupplierOrganizationIdAndWorkOrderNumber(
                supplierOrganizationId, workOrderNumber) > 0;
    }
}