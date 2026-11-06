package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.adapters;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemCode;
import com.innovacorp.reliant.platform.equipment.domain.repositories.HvofSystemRepository;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.assemblers.HvofSystemPersistenceAssembler;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.repositories.HvofSystemPersistenceRepository;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository adapter that bridges the HVOF system repository port with Spring Data JPA.
 * Also the event-publishing boundary: a brand-new system publishes HvofSystemRegisteredEvent after save.
 */
@Repository
public class HvofSystemRepositoryImpl implements HvofSystemRepository {

    private final HvofSystemPersistenceRepository persistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public HvofSystemRepositoryImpl(HvofSystemPersistenceRepository persistenceRepository, DomainEventPublisher domainEventPublisher) {
        this.persistenceRepository = persistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public HvofSystem save(HvofSystem hvofSystem) {
        boolean isNew = hvofSystem.getId() == null;
        var savedEntity = persistenceRepository.save(HvofSystemPersistenceAssembler.toPersistenceFromDomain(hvofSystem));
        var saved = HvofSystemPersistenceAssembler.toDomainFromPersistence(savedEntity);
        if (isNew) {
            saved.onCreated();
            domainEventPublisher.publishAndClearEvents(saved);
        }
        return saved;
    }

    @Override
    public boolean existsByOrganizationIdAndCode(OrganizationId organizationId, HvofSystemCode code) {
        return persistenceRepository.countByOrganizationIdAndCode(organizationId, code) > 0;
    }

    @Override
    public Optional<HvofSystem> findById(Long id) {
        return persistenceRepository.findById(id).map(HvofSystemPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<HvofSystem> findAllByOrganizationId(OrganizationId organizationId) {
        return persistenceRepository.findAllByOrganizationId(organizationId).stream()
                .map(HvofSystemPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}