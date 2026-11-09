package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.adapters;

import com.innovacorp.reliant.platform.shared.infrastructure.events.DomainEventPublisher;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.RecoveredComponent;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;
import com.innovacorp.reliant.platform.traceability.domain.repositories.RecoveredComponentRepository;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.assemblers.RecoveredComponentPersistenceAssembler;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.repositories.RecoveredComponentPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RecoveredComponentRepositoryImpl implements RecoveredComponentRepository {

    private final RecoveredComponentPersistenceRepository persistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public RecoveredComponentRepositoryImpl(RecoveredComponentPersistenceRepository persistenceRepository,
                                            DomainEventPublisher domainEventPublisher) {
        this.persistenceRepository = persistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public RecoveredComponent save(RecoveredComponent component) {
        boolean isNew = component.getId() == null;
        var savedEntity = persistenceRepository.save(
                RecoveredComponentPersistenceAssembler.toPersistenceFromDomain(component));
        var saved = RecoveredComponentPersistenceAssembler.toDomainFromPersistence(savedEntity);

        if (isNew) {
            saved.onCreated();
            domainEventPublisher.publishAndClearEvents(saved);
        }

        return saved;
    }

    @Override
    public Optional<RecoveredComponent> findById(Long id) {
        return persistenceRepository.findById(id)
                .map(RecoveredComponentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<RecoveredComponent> findAllByCustomerId(CustomerId customerId) {
        return persistenceRepository.findAllByCustomerId(customerId).stream()
                .map(RecoveredComponentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsByCustomerIdAndSerialNumber(CustomerId customerId, String serialNumber) {
        return persistenceRepository.countByCustomerIdAndSerialNumber(customerId, serialNumber) > 0;
    }
}