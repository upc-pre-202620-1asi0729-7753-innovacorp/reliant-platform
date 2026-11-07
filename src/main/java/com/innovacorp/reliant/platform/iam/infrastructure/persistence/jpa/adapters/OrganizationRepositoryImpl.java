package com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.adapters;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.Organization;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.Ruc;
import com.innovacorp.reliant.platform.iam.domain.repositories.OrganizationRepository;
import com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.assemblers.OrganizationPersistenceAssembler;
import com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.repositories.OrganizationPersistenceRepository;
import com.innovacorp.reliant.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrganizationRepositoryImpl implements OrganizationRepository {

    private final OrganizationPersistenceRepository persistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public OrganizationRepositoryImpl(OrganizationPersistenceRepository persistenceRepository, DomainEventPublisher domainEventPublisher) {
        this.persistenceRepository = persistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Organization save(Organization organization) {
        boolean isNew = organization.getId() == null;
        var savedEntity = persistenceRepository.save(OrganizationPersistenceAssembler.toPersistenceFromDomain(organization));
        var saved = OrganizationPersistenceAssembler.toDomainFromPersistence(savedEntity);
        if (isNew) {
            saved.onCreated();
            domainEventPublisher.publishAndClearEvents(saved);
        }
        return saved;
    }

    @Override
    public boolean existsByRuc(Ruc ruc) {
        return persistenceRepository.countByRuc(ruc) > 0;
    }

    @Override
    public Optional<Organization> findById(Long id) {
        return persistenceRepository.findById(id).map(OrganizationPersistenceAssembler::toDomainFromPersistence);
    }
}
